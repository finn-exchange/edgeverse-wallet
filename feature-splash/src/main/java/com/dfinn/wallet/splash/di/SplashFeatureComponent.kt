package com.dfinn.wallet.splash.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.splash.SplashRouter
import com.dfinn.wallet.splash.presentation.di.SplashComponent

@Component(
    dependencies = [
        SplashFeatureDependencies::class
    ],
    modules = [
        SplashFeatureModule::class
    ]
)
@FeatureScope
interface SplashFeatureComponent : SplashFeatureApi {

    fun splashComponentFactory(): SplashComponent.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun router(splashRouter: SplashRouter): Builder

        fun withDependencies(deps: SplashFeatureDependencies): Builder

        fun build(): SplashFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            AccountFeatureApi::class
        ]
    )
    interface SplashFeatureDependenciesComponent : SplashFeatureDependencies
}
