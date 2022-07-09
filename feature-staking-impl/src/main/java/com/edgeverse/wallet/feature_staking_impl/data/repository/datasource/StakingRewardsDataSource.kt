package com.edgeverse.wallet.feature_staking_impl.data.repository.datasource

import com.edgeverse.wallet.feature_staking_impl.domain.model.TotalReward
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow

interface StakingRewardsDataSource {

    fun totalRewardsFlow(accountAddress: String): Flow<TotalReward>

    suspend fun sync(accountAddress: String, chain: Chain)
}
