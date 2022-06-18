package com.dfinn.wallet.feature_staking_impl.data.repository

import com.dfinn.wallet.feature_staking_impl.data.repository.datasource.StakingRewardsDataSource
import com.dfinn.wallet.feature_staking_impl.domain.model.TotalReward
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow

class StakingRewardsRepository(
    private val stakingRewardsDataSource: StakingRewardsDataSource,
) {

    fun totalRewardFlow(accountAddress: String): Flow<TotalReward> {
        return stakingRewardsDataSource.totalRewardsFlow(accountAddress)
    }

    suspend fun sync(accountAddress: String, chain: Chain) {
        stakingRewardsDataSource.sync(accountAddress, chain)
    }
}
