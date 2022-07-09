package com.edgeverse.wallet.feature_staking_api.di

import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository

interface StakingFeatureApi {

    fun repository(): StakingRepository
}
