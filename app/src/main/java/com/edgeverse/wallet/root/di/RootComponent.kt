package com.edgeverse.wallet.root.di

import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.root.presentation.RootRouter
import com.edgeverse.wallet.root.presentation.di.RootActivityComponent
import com.edgeverse.wallet.root.presentation.main.di.MainFragmentComponent
import com.edgeverse.wallet.runtime.di.RuntimeApi
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        RootDependencies::class
    ],
    modules = [
        RootFeatureModule::class
    ]
)
@FeatureScope
interface RootComponent {

    fun mainActivityComponentFactory(): RootActivityComponent.Factory

    fun mainFragmentComponentFactory(): MainFragmentComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance navigationHolder: NavigationHolder,
            @BindsInstance rootRouter: RootRouter,
            deps: RootDependencies
        ): RootComponent
    }

    @Component(
        dependencies = [
            AccountFeatureApi::class,
            WalletFeatureApi::class,
            StakingFeatureApi::class,
            CrowdloanFeatureApi::class,
            AssetsFeatureApi::class,
            DbApi::class,
            CommonApi::class,
            RuntimeApi::class
        ]
    )
    interface RootFeatureDependenciesComponent :
        RootDependencies
}
