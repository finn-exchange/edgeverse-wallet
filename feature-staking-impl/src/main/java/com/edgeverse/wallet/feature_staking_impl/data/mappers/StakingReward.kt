package com.edgeverse.wallet.feature_staking_impl.data.mappers

import com.edgeverse.wallet.core_db.model.TotalRewardLocal
import com.edgeverse.wallet.feature_staking_impl.domain.model.TotalReward

fun mapTotalRewardLocalToTotalReward(reward: TotalRewardLocal): TotalReward {
    return reward.totalReward
}
