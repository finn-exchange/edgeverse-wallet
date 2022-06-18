package com.dfinn.wallet.feature_wallet_impl.di

import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.feature_wallet_impl.di.modules.AssetsModule
import com.dfinn.wallet.feature_wallet_impl.di.modules.ValidationsModule
import com.dfinn.wallet.runtime.di.RuntimeApi

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
