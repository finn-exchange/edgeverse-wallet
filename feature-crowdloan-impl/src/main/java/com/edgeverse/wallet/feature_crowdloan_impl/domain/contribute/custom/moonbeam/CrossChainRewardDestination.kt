package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

class CrossChainRewardDestination(
    val addressInDestination: String,
    val destination: Chain,
)
