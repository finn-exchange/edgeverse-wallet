package com.edgeverse.wallet.feature_wallet_impl.di

import dagger.Component
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.feature_wallet_impl.di.modules.AssetsModule
import com.edgeverse.wallet.feature_wallet_impl.di.modules.ValidationsModule
import com.edgeverse.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        WalletFeatureDependencies::class
    ],
    modules = [
        WalletFeatureModule::class,
        ValidationsModule::class,
        AssetsModule::class,
    ]
)
@FeatureScope
interface WalletFeatureComponent : WalletFeatureApi {

    @Component.Factory
    interface Factory {

        fun create(
            deps: WalletFeatureDependencies
        ): WalletFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            RuntimeApi::class,
            AccountFeatureApi::class
        ]
    )
    interface WalletFeatureDependenciesComponent : WalletFeatureDependencies
}
