package com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.states

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.polkadotJs.PolkadotJsExtensionInteractor
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsTransportRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.model.PolkadotJsSignRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.model.SignerResult
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.states.BaseState
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3ExtensionStateMachine.ExternalEvent
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3ExtensionStateMachine.StateMachineTransition
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3StateMachineHost
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3StateMachineHost.NotAuthorizedException
import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.ConfirmTxResponse
import kotlinx.coroutines.flow.flowOf

class DefaultPolkadotJsState(
    private val interactor: PolkadotJsExtensionInteractor,
    commonInteractor: DappInteractor,
    resourceManager: ResourceManager,
    addressIconGenerator: AddressIconGenerator,
    web3Session: Web3Session,
    hostApi: Web3StateMachineHost,
) : BaseState<PolkadotJsTransportRequest<*>, PolkadotJsState>(
        commonInteractor = commonInteractor,
        resourceManager = resourceManager,
        addressIconGenerator = addressIconGenerator,
        web3Session = web3Session,
        hostApi = hostApi
    ),
    PolkadotJsState {

    override suspend fun acceptRequest(request: PolkadotJsTransportRequest<*>, transition: StateMachineTransition<PolkadotJsState>) {
        when (request) {
            is PolkadotJsTransportRequest.Single.AuthorizeTab -> authorizeTab(request)
            is PolkadotJsTransportRequest.Single.ListAccounts -> supplyAccountList(request)
            is PolkadotJsTransportRequest.Single.Sign -> signExtrinsicIfAllowed(request, getAuthorizationStateForCurrentPage())
            is PolkadotJsTransportRequest.Subscription.SubscribeAccounts -> supplyAccountListSubscription(request)
            is PolkadotJsTransportRequest.Single.ListMetadata -> suppleKnownMetadatas(request)
            is PolkadotJsTransportRequest.Single.ProvideMetadata -> handleProvideMetadata(request)
        }
    }

    override suspend fun acceptEvent(event: ExternalEvent, transition: StateMachineTransition<PolkadotJsState>) {
        when (event) {
            ExternalEvent.PhishingDetected -> transition.emitState(PhishingDetectedPolkadotJsState())
        }
    }

    private suspend fun authorizeTab(request: PolkadotJsTransportRequest.Single.AuthorizeTab) {
        val authorized = authorizeDapp()

        request.accept(authorized)
    }

    private suspend fun signExtrinsicIfAllowed(request: PolkadotJsTransportRequest.Single.Sign, state: Web3Session.Authorization.State) {
        when (state) {
            // request user confirmation if dapp is authorized
            Web3Session.Authorization.State.ALLOWED -> signExtrinsicWithConfirmation(request)
            // reject otherwise
            else -> request.reject(NotAuthorizedException)
        }
    }

    private suspend fun signExtrinsicWithConfirmation(request: PolkadotJsTransportRequest.Single.Sign) {
        val signRequest = PolkadotJsSignRequest(request.requestId, request.signerPayload)

        when (val response = hostApi.confirmTx(signRequest)) {
            is ConfirmTxResponse.Rejected -> request.reject(NotAuthorizedException)
            is ConfirmTxResponse.Sent -> throw IllegalStateException("Unexpected 'Sent' response for PolkadotJs extension")
            is ConfirmTxResponse.Signed -> request.accept(SignerResult(response.requestId, response.signature))
            is ConfirmTxResponse.SigningFailed -> {
                hostApi.showError(resourceManager.getString(R.string.dapp_sign_extrinsic_failed))

                request.reject(Web3StateMachineHost.SigningFailedException)
            }
        }
    }

    private suspend fun handleProvideMetadata(request: PolkadotJsTransportRequest.Single.ProvideMetadata) = request.respondIfAllowed {
        false // we do not accept provided metadata since app handles metadata sync by its own
    }

    private suspend fun suppleKnownMetadatas(request: PolkadotJsTransportRequest.Single.ListMetadata) = request.respondIfAllowed {
        interactor.getKnownInjectedMetadatas()
    }

    private suspend fun supplyAccountList(request: PolkadotJsTransportRequest.Single.ListAccounts) = request.respondIfAllowed {
        interactor.getInjectedAccounts()
    }

    private suspend fun supplyAccountListSubscription(request: PolkadotJsTransportRequest.Subscription.SubscribeAccounts) {
        request.respondIfAllowed {
            flowOf(interactor.getInjectedAccounts())
        }
    }

    private suspend fun <T> PolkadotJsTransportRequest<T>.respondIfAllowed(
        responseConstructor: suspend () -> T
    ) = respondIfAllowed(
        ifAllowed = { accept(responseConstructor()) }
    ) { reject(NotAuthorizedException) }
}
