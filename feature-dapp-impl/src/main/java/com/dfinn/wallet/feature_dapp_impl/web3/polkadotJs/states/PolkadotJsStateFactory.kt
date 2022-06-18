package com.dfinn.wallet.feature_dapp_impl.web3.polkadotJs.states

import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_dapp_impl.domain.DappInteractor
import com.dfinn.wallet.feature_dapp_impl.domain.browser.polkadotJs.PolkadotJsExtensionInteractor
import com.dfinn.wallet.feature_dapp_impl.web3.session.Web3Session
import com.dfinn.wallet.feature_dapp_impl.web3.states.Web3StateMachineHost

class PolkadotJsStateFactory(
    private val interactor: PolkadotJsExtensionInteractor,
    private val commonInteractor: DappInteractor,
    private val resourceManager: ResourceManager,
    private val addressIconGenerator: AddressIconGenerator,
    private val web3Session: Web3Session,
) {

    fun default(hostApi: Web3StateMachineHost): DefaultPolkadotJsState {
        return DefaultPolkadotJsState(
            interactor = interactor,
            commonInteractor = commonInteractor,
            resourceManager = resourceManager,
            addressIconGenerator = addressIconGenerator,
            web3Session = web3Session,
            hostApi = hostApi
        )
    }
}
