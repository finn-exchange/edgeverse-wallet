package com.edgeverse.wallet.feature_dapp_impl.web3.metamask.states

import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.model.MetamaskChain
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskTransportRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.states.PhishingDetectedState

class PhishingDetectedMetamaskState(override val chain: MetamaskChain) :
    PhishingDetectedState<MetamaskTransportRequest<*>, MetamaskState>(), MetamaskState {

    override val selectedAccountAddress: String? = null
}
