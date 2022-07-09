package com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.parallel

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parallel.ParallelApi

@Module
class ParallelContributionModule {

    @Provides
    @FeatureScope
    fun provideParallelApi(
        networkApiCreator: NetworkApiCreator,
    ) = networkApiCreator.create(ParallelApi::class.java, customBaseUrl = ParallelApi.BASE_URL)
}
