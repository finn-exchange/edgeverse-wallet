package com.dfinn.wallet.feature_dapp_impl.web3.polkadotJs.states

import com.dfinn.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsTransportRequest
import com.dfinn.wallet.feature_dapp_impl.web3.states.PhishingDetectedState

class PhishingDetectedPolkadotJsState : PhishingDetectedState<PolkadotJsTransportRequest<*>, PolkadotJsState>(), PolkadotJsState
