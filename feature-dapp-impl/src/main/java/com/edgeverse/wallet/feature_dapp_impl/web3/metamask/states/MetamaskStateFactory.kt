package com.edgeverse.wallet.feature_dapp_impl.web3.metamask.states

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.metamask.MetamaskInteractor
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.model.MetamaskChain
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3StateMachineHost

class MetamaskStateFactory(
    private val interactor: MetamaskInteractor,
    private val commonInteractor: DappInteractor,
    private val resourceManager: ResourceManager,
    private val addressIconGenerator: AddressIconGenerator,
    private val web3Session: Web3Session,
) {

    fun default(
        hostApi: Web3StateMachineHost,
        chain: MetamaskChain = MetamaskChain.ETHEREUM,
        selectedAddress: String? = null
    ): DefaultMetamaskState {
        return DefaultMetamaskState(
            interactor = interactor,
            commonInteractor = commonInteractor,
            resourceManager = resourceManager,
            addressIconGenerator = addressIconGenerator,
            web3Session = web3Session,
            hostApi = hostApi,
            chain = chain,
            selectedAccountAddress = selectedAddress,
            stateFactory = this
        )
    }
}
