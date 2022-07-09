package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.mixin.actionAwaitable.confirmingAction
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.event
import com.edgeverse.wallet.common.utils.singleReplaySharedFlow
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.BrowserPage
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.BrowserPageAnalyzed
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.DappBrowserInteractor
import com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites.AddToFavouritesPayload
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignPayload
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignRequester
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.awaitConfirmation
import com.edgeverse.wallet.feature_dapp_impl.presentation.common.favourites.RemoveFavouritesPayload
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DAppSearchRequester
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.SearchPayload
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session.Authorization.State
import com.edgeverse.wallet.feature_dapp_impl.web3.states.ExtensionStoreFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3ExtensionStateMachine.ExternalEvent
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3StateMachineHost
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.AuthorizeDAppPayload
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.ConfirmTxRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.ConfirmTxResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

enum class ConfirmationState {
    ALLOWED, REJECTED, CANCELLED
}

class DAppBrowserViewModel(
    private val router: DAppRouter,
    private val signRequester: DAppSignRequester,
    private val extensionStoreFactory: ExtensionStoreFactory,
    private val interactor: DappBrowserInteractor,
    private val dAppSearchRequester: DAppSearchRequester,
    private val initialUrl: String,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val actionAwaitableMixinFactory: ActionAwaitableMixin.Factory
) : BaseViewModel(), Web3StateMachineHost {

    private val _showConfirmationDialog = MutableLiveData<Event<DappPendingConfirmation<*>>>()
    val showConfirmationSheet = _showConfirmationDialog

    override val selectedAccount = selectedAccountUseCase.selectedMetaAccountFlow()
        .share()

    private val currentPage = singleReplaySharedFlow<BrowserPage>()

    override val currentPageAnalyzed = currentPage.flatMapLatest {
        interactor.observeBrowserPageFor(it)
    }.shareInBackground()

    override val externalEvents = singleReplaySharedFlow<ExternalEvent>()

    private val _browserNavigationCommandEvent = MutableLiveData<Event<BrowserNavigationCommand>>()
    val browserNavigationCommandEvent: LiveData<Event<BrowserNavigationCommand>> = _browserNavigationCommandEvent

    val removeFromFavouritesConfirmation = actionAwaitableMixinFactory.confirmingAction<RemoveFavouritesPayload>()

    val extensionsStore = extensionStoreFactory.create(hostApi = this, coroutineScope = this)

    init {
        dAppSearchRequester.responseFlow
            .onEach { it.newUrl?.let(::forceLoad) }
            .launchIn(this)

        watchDangerousWebsites()

        forceLoad(initialUrl)
    }

    override suspend fun authorizeDApp(payload: AuthorizeDAppPayload): State {
        val confirmationState = awaitConfirmation(DappPendingConfirmation.Action.Authorize(payload))

        return mapConfirmationStateToAuthorizationState(confirmationState)
    }

    override suspend fun confirmTx(request: ConfirmTxRequest): ConfirmTxResponse {
        val response = withContext(Dispatchers.Main) {
            signRequester.awaitConfirmation(mapSignExtrinsicRequestToPayload(request))
        }

        return when (response) {
            is DAppSignCommunicator.Response.Rejected -> ConfirmTxResponse.Rejected(response.requestId)
            is DAppSignCommunicator.Response.Signed -> ConfirmTxResponse.Signed(response.requestId, response.signature)
            is DAppSignCommunicator.Response.SigningFailed -> ConfirmTxResponse.SigningFailed(response.requestId)
            is DAppSignCommunicator.Response.Sent -> ConfirmTxResponse.Sent(response.requestId, response.txHash)
        }
    }

    override fun reloadPage() {
        _browserNavigationCommandEvent.postValue(BrowserNavigationCommand.Reload.event())
    }

    fun onPageChanged(url: String, title: String?) {
        updateCurrentPage(url, title, synchronizedWithBrowser = true)
    }

    fun closeClicked() = launch {
        val confirmationState = awaitConfirmation(DappPendingConfirmation.Action.CloseScreen)

        if (confirmationState == ConfirmationState.ALLOWED) {
            exitBrowser()
        }
    }

    fun openSearch() = launch {
        val currentPage = currentPage.first()

        dAppSearchRequester.openRequest(SearchPayload(initialUrl = currentPage.url))
    }

    fun onFavouriteClicked() = launch {
        val page = currentPageAnalyzed.first()

        if (page.isFavourite) {
            val dAppTitle = page.title ?: page.display
            removeFromFavouritesConfirmation.awaitAction(dAppTitle)

            interactor.removeDAppFromFavourites(page.url)
        } else {
            val payload = AddToFavouritesPayload(
                url = page.url,
                label = page.title,
                iconLink = null
            )

            router.openAddToFavourites(payload)
        }
    }

    private fun watchDangerousWebsites() {
        currentPageAnalyzed
            .filter { it.synchronizedWithBrowser && it.security == BrowserPageAnalyzed.Security.DANGEROUS }
            .distinctUntilChanged()
            .onEach {
                externalEvents.emit(ExternalEvent.PhishingDetected)

                awaitConfirmation(DappPendingConfirmation.Action.AcknowledgePhishingAlert)

                exitBrowser()
            }
            .launchIn(this)
    }

    private fun forceLoad(url: String) {
        _browserNavigationCommandEvent.value = BrowserNavigationCommand.OpenUrl(url).event()

        updateCurrentPage(url, title = null, synchronizedWithBrowser = false)
    }

    private suspend fun awaitConfirmation(action: DappPendingConfirmation.Action) = suspendCoroutine<ConfirmationState> {
        val confirmation = DappPendingConfirmation(
            onConfirm = { it.resume(ConfirmationState.ALLOWED) },
            onDeny = { it.resume(ConfirmationState.REJECTED) },
            onCancel = { it.resume(ConfirmationState.CANCELLED) },
            action = action
        )

        _showConfirmationDialog.postValue(confirmation.event())
    }

    private fun mapConfirmationStateToAuthorizationState(
        confirmationState: ConfirmationState
    ): State = when (confirmationState) {
        ConfirmationState.ALLOWED -> State.ALLOWED
        ConfirmationState.REJECTED -> State.REJECTED
        ConfirmationState.CANCELLED -> State.NONE
    }

    private fun exitBrowser() = router.back()

    private fun updateCurrentPage(
        url: String,
        title: String?,
        synchronizedWithBrowser: Boolean
    ) = launch {
        currentPage.emit(BrowserPage(url, title, synchronizedWithBrowser))
    }

    private suspend fun mapSignExtrinsicRequestToPayload(request: ConfirmTxRequest) = DAppSignPayload(
        body = request,
        dappUrl = currentPageAnalyzed.first().url
    )
}
