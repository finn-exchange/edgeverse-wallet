package com.dfinn.wallet.feature_dapp_impl.web3.metamask.states

import com.dfinn.wallet.feature_dapp_impl.web3.metamask.model.MetamaskChain
import com.dfinn.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskTransportRequest
import com.dfinn.wallet.feature_dapp_impl.web3.states.Web3ExtensionStateMachine

typealias MetamaskStateMachine = Web3ExtensionStateMachine<MetamaskState>

interface MetamaskState : Web3ExtensionStateMachine.State<MetamaskTransportRequest<*>, MetamaskState> {

    val chain: MetamaskChain

    val selectedAccountAddress: String?
}
