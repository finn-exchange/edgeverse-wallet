package com.edgeverse.wallet.feature_dapp_impl.web3.states

import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.BrowserPageAnalyzed
import com.edgeverse.wallet.feature_dapp_impl.web3.Web3Transport
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.AuthorizeDAppPayload
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.ConfirmTxRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.ConfirmTxResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

interface Web3ExtensionStateMachine<S> {

    val state: StateFlow<S>

    suspend fun transition(transition: suspend (StateMachineTransition<S>.(state: S) -> Unit))

    interface State<R : Web3Transport.Request<*>, S> {

        suspend fun acceptRequest(request: R, transition: StateMachineTransition<S>)

        suspend fun acceptEvent(event: ExternalEvent, transition: StateMachineTransition<S>)
    }

    sealed class ExternalEvent {

        object PhishingDetected : ExternalEvent()
    }

    interface StateMachineTransition<S> {

        fun emitState(newState: S)

        suspend fun <R : Web3Transport.Request<*>> State<R, S>.acceptRequest(request: R) = acceptRequest(request, this@StateMachineTransition)
        suspend fun State<*, S>.acceptEvent(event: ExternalEvent) = acceptEvent(event, this@StateMachineTransition)
    }
}

interface Web3StateMachineHost {

    object NotAuthorizedException : Exception("Rejected by user")
    object SigningFailedException : Exception("Signing failed")

    val selectedAccount: Flow<MetaAccount>
    val currentPageAnalyzed: Flow<BrowserPageAnalyzed>

    val externalEvents: Flow<Web3ExtensionStateMachine.ExternalEvent>

    suspend fun authorizeDApp(payload: AuthorizeDAppPayload): Web3Session.Authorization.State
    suspend fun confirmTx(request: ConfirmTxRequest): ConfirmTxResponse

    fun showError(text: String)

    fun reloadPage()
}

suspend fun Web3StateMachineHost.selectedMetaAccountId() = selectedAccount.first().id
