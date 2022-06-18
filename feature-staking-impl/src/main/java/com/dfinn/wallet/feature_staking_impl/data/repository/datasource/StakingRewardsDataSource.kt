package com.dfinn.wallet.feature_staking_impl.data.repository.datasource

import com.dfinn.wallet.feature_staking_impl.domain.model.TotalReward
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow

interface StakingRewardsDataSource {

    fun totalRewardsFlow(accountAddress: String): Flow<TotalReward>

    suspend fun sync(accountAddress: String, chain: Chain)
}
