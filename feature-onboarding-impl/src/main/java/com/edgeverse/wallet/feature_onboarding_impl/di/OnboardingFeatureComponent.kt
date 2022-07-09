package com.edgeverse.wallet.feature_onboarding_impl.di

import dagger.BindsInstance
import dagger.Component
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_onboarding_api.di.OnboardingFeatureApi
import com.edgeverse.wallet.feature_onboarding_impl.OnboardingRouter
import com.edgeverse.wallet.feature_onboarding_impl.presentation.welcome.di.WelcomeComponent

@Component(
    dependencies = [
        OnboardingFeatureDependencies::class
    ],
    modules = [
        OnboardingFeatureModule::class
    ]
)
@FeatureScope
interface OnboardingFeatureComponent : OnboardingFeatureApi {

    fun welcomeComponentFactory(): WelcomeComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance onboardingRouter: OnboardingRouter,
            deps: OnboardingFeatureDependencies
        ): OnboardingFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            AccountFeatureApi::class
        ]
    )
    interface OnboardingFeatureDependenciesComponent : OnboardingFeatureDependencies
}
