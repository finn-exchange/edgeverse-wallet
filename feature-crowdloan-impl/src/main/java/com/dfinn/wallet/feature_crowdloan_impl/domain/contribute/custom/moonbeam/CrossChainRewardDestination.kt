package com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam

import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain

class CrossChainRewardDestination(
    val addressInDestination: String,
    val destination: Chain,
)
