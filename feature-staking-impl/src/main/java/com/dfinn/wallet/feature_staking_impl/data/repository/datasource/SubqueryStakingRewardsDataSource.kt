package com.dfinn.wallet.feature_staking_impl.data.repository.datasource

import com.dfinn.wallet.core_db.dao.StakingTotalRewardDao
import com.dfinn.wallet.core_db.model.TotalRewardLocal
import com.dfinn.wallet.feature_staking_impl.data.mappers.mapTotalRewardLocalToTotalReward
import com.dfinn.wallet.feature_staking_impl.data.network.subquery.StakingApi
import com.dfinn.wallet.feature_staking_impl.data.network.subquery.request.StakingSumRewardRequest
import com.dfinn.wallet.feature_staking_impl.data.network.subquery.response.totalReward
import com.dfinn.wallet.feature_staking_impl.domain.model.TotalReward
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class SubqueryStakingRewardsDataSource(
    private val stakingApi: StakingApi,
    private val stakingTotalRewardDao: StakingTotalRewardDao,
) : StakingRewardsDataSource {

    override fun totalRewardsFlow(accountAddress: String): Flow<TotalReward> {
        return stakingTotalRewardDao.observeTotalRewards(accountAddress)
            .filterNotNull()
            .map(::mapTotalRewardLocalToTotalReward)
    }

    override suspend fun sync(accountAddress: String, chain: Chain) {
        val stakingExternalApi = chain.externalApi?.staking ?: return

        val response = stakingApi.getTotalReward(
            url = stakingExternalApi.url,
            body = StakingSumRewardRequest(accountAddress = accountAddress)
        )
        val totalResult = response.data.totalReward

        stakingTotalRewardDao.insert(TotalRewardLocal(accountAddress, totalResult))
    }
}
