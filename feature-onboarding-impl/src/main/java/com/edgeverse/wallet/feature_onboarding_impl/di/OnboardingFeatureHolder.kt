package com.edgeverse.wallet.feature_onboarding_impl.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_onboarding_impl.OnboardingRouter
import javax.inject.Inject

@ApplicationScope
class OnboardingFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val onboardingRouter: OnboardingRouter
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val onboardingFeatureDependencies = DaggerOnboardingFeatureComponent_OnboardingFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .build()
        return DaggerOnboardingFeatureComponent.factory()
            .create(onboardingRouter, onboardingFeatureDependencies)
    }
}
