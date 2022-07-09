package com.edgeverse.wallet.feature_dapp_impl.presentation.authorizedDApps

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.mixin.actionAwaitable.confirmingAction
import com.edgeverse.wallet.common.utils.mapList
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.domain.authorizedDApps.AuthorizedDApp
import com.edgeverse.wallet.feature_dapp_impl.domain.authorizedDApps.AuthorizedDAppsInteractor
import com.edgeverse.wallet.feature_dapp_impl.presentation.authorizedDApps.model.AuthorizedDAppModel
import kotlinx.coroutines.launch

typealias RevokeAuthorizationPayload = String // dApp name

class AuthorizedDAppsViewModel(
    private val interactor: AuthorizedDAppsInteractor,
    private val router: DAppRouter,
    private val actionAwaitableMixinFactory: ActionAwaitableMixin.Factory,
    walletUiUseCase: WalletUiUseCase,
) : BaseViewModel() {

    val revokeAuthorizationConfirmation = actionAwaitableMixinFactory.confirmingAction<RevokeAuthorizationPayload>()

    val walletUi = walletUiUseCase.selectedWalletUiFlow(showAddressIcon = true)
        .shareInBackground()

    val authorizedDApps = interactor.observeAuthorizedDApps()
        .mapList(::mapAuthorizedDAppToModel)
        .shareInBackground()

    fun backClicked() {
        router.back()
    }

    fun revokeClicked(item: AuthorizedDAppModel) = launch {
        val dAppTitle = item.title ?: item.url
        revokeAuthorizationConfirmation.awaitAction(dAppTitle)

        interactor.revokeAuthorization(item.url)
    }

    private fun mapAuthorizedDAppToModel(
        authorizedDApp: AuthorizedDApp
    ): AuthorizedDAppModel {
        return AuthorizedDAppModel(
            title = authorizedDApp.name,
            url = authorizedDApp.baseUrl,
            iconLink = authorizedDApp.iconLink
        )
    }
}
