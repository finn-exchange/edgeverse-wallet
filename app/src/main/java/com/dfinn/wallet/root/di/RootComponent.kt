package com.dfinn.wallet.root.di

import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_assets.di.AssetsFeatureApi
import com.dfinn.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.root.navigation.NavigationHolder
import com.dfinn.wallet.root.presentation.RootRouter
import com.dfinn.wallet.root.presentation.di.RootActivityComponent
import com.dfinn.wallet.root.presentation.main.di.MainFragmentComponent
import com.dfinn.wallet.runtime.di.RuntimeApi
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
