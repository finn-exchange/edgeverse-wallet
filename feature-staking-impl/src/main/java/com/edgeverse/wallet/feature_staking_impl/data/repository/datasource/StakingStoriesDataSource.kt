package com.edgeverse.wallet.feature_staking_impl.data.repository.datasource

import com.edgeverse.wallet.feature_staking_api.domain.model.StakingStory
import kotlinx.coroutines.flow.Flow

interface StakingStoriesDataSource {

    fun getStoriesFlow(): Flow<List<StakingStory>>
}
