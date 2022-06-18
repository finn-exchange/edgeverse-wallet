package com.dfinn.wallet.feature_onboarding_impl.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_onboarding_api.di.OnboardingFeatureApi
import com.dfinn.wallet.feature_onboarding_impl.OnboardingRouter
import com.dfinn.wallet.feature_onboarding_impl.presentation.welcome.di.WelcomeComponent

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
