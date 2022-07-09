package com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.states

import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsTransportRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.states.Web3ExtensionStateMachine

typealias PolkadotJsStateMachine = Web3ExtensionStateMachine<PolkadotJsState>

interface PolkadotJsState : Web3ExtensionStateMachine.State<PolkadotJsTransportRequest<*>, PolkadotJsState>
