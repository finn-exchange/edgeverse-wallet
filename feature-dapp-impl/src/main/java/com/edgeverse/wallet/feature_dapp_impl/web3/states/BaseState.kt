package com.edgeverse.wallet.feature_dapp_impl.web3.states

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.createAddressModel
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.domain.model.defaultSubstrateAddress
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.web3.Web3Transport
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.AuthorizeDAppPayload
import kotlinx.coroutines.flow.first

abstract class BaseState<R : Web3Transport.Request<*>, S>(
    protected val commonInteractor: DappInteractor,
    protected val resourceManager: ResourceManager,
    protected val addressIconGenerator: AddressIconGenerator,
    protected val web3Session: Web3Session,
    protected val hostApi: Web3StateMachineHost,
) : Web3ExtensionStateMachine.State<R, S> {

    suspend fun respondIfAllowed(
        ifAllowed: suspend () -> Unit,
        ifDenied: suspend () -> Unit
    ) = if (getAuthorizationStateForCurrentPage() == Web3Session.Authorization.State.ALLOWED) {
        ifAllowed()
    } else {
        ifDenied()
    }

    protected suspend fun authorizeDapp(): Boolean {
        return when (getAuthorizationStateForCurrentPage()) {
            // user already accepted - no need to ask second time
            Web3Session.Authorization.State.ALLOWED -> true
            // first time dapp request authorization during this session
            Web3Session.Authorization.State.NONE -> authorizePageWithConfirmation()
            // user rejected this dapp previosuly - ask for authorization one more time
            Web3Session.Authorization.State.REJECTED -> authorizePageWithConfirmation()
        }
    }

    protected suspend fun getAuthorizationStateForCurrentPage(): Web3Session.Authorization.State {
        return web3Session.authorizationStateFor(hostApi.currentPageAnalyzed.first().url, hostApi.selectedMetaAccountId())
    }

    /**
     * @return whether authorization request was approved or not
     */
    private suspend fun authorizePageWithConfirmation(): Boolean {
        val currentPage = hostApi.currentPageAnalyzed.first()
        val selectedAccount = hostApi.selectedAccount.first()
        // use url got from browser instead of url got from dApp to prevent dApp supplying wrong URL
        val dAppInfo = commonInteractor.getDAppInfo(dAppUrl = currentPage.url)

        val dAppIdentifier = dAppInfo.metadata?.name ?: dAppInfo.baseUrl

        val action = AuthorizeDAppPayload(
            title = resourceManager.getString(
                com.edgeverse.wallet.feature_dapp_impl.R.string.dapp_confirm_authorize_title_format,
                dAppIdentifier
            ),
            dAppIconUrl = dAppInfo.metadata?.iconLink,
            dAppUrl = dAppInfo.baseUrl,
            walletAddressModel = addressIconGenerator.createAddressModel(
                accountAddress = selectedAccount.defaultSubstrateAddress,
                sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
                accountName = selectedAccount.name,
                background = AddressIconGenerator.BACKGROUND_TRANSPARENT
            )
        )

        val authorizationState = hostApi.authorizeDApp(action)

        web3Session.updateAuthorization(
            state = authorizationState,
            fullUrl = currentPage.url,
            dAppTitle = dAppIdentifier,
            metaId = selectedAccount.id
        )

        return authorizationState == Web3Session.Authorization.State.ALLOWED
    }
}
