package com.dfinn.wallet.feature_staking_impl.data.mappers

import com.dfinn.wallet.core_db.model.TotalRewardLocal
import com.dfinn.wallet.feature_staking_impl.domain.model.TotalReward

fun mapTotalRewardLocalToTotalReward(reward: TotalRewardLocal): TotalReward {
    return reward.totalReward
}
