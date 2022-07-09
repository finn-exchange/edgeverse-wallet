package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.model

import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountModel

data class StakingNetworkInfoModel(
    val totalStaked: AmountModel,
    val minimumStake: AmountModel,
    val activeNominators: String,
    val stakingPeriod: String,
    val unstakingPeriod: String
)
