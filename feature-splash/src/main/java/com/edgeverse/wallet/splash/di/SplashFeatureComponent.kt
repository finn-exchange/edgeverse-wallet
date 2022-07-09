package com.edgeverse.wallet.splash.di

import dagger.BindsInstance
import dagger.Component
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.splash.SplashRouter
import com.edgeverse.wallet.splash.presentation.di.SplashComponent

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
