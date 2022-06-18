package com.dfinn.wallet.feature_crowdloan_api.di

import com.dfinn.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository

interface CrowdloanFeatureApi {

    fun repository(): CrowdloanRepository
}
