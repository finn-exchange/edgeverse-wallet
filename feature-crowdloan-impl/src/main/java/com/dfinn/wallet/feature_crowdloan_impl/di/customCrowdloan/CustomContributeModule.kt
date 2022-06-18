package com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.acala.AcalaContributionModule
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.astar.AstarContributionModule
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.bifrost.BifrostContributionModule
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.moonbeam.MoonbeamContributionModule
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.parallel.ParallelContributionModule

@Module(
    includes = [
        AcalaContributionModule::class,
        BifrostContributionModule::class,
        MoonbeamContributionModule::class,
        AstarContributionModule::class,
        ParallelContributionModule::class
    ]
)
class CustomContributeModule {

    @Provides
    @FeatureScope
    fun provideCustomContributionManager(
        factories: @JvmSuppressWildcards Set<CustomContributeFactory>,
    ) = CustomContributeManager(factories)
}
