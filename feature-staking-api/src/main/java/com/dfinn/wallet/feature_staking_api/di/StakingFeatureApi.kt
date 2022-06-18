package com.dfinn.wallet.feature_staking_api.di

import com.dfinn.wallet.feature_staking_api.domain.api.StakingRepository

interface StakingFeatureApi {

    fun repository(): StakingRepository
}
