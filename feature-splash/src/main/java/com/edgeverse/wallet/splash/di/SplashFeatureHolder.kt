package com.edgeverse.wallet.splash.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.splash.SplashRouter
import javax.inject.Inject

@ApplicationScope
class SplashFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val splashRouter: SplashRouter
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val splashFeatureDependencies = DaggerSplashFeatureComponent_SplashFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .build()
        return DaggerSplashFeatureComponent.builder()
            .withDependencies(splashFeatureDependencies)
            .router(splashRouter)
            .build()
    }
}
