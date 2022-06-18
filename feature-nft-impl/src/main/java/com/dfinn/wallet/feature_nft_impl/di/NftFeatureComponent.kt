package com.dfinn.wallet.feature_nft_impl.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_nft_api.NftFeatureApi
import com.dfinn.wallet.feature_nft_impl.NftRouter
import com.dfinn.wallet.feature_nft_impl.presentation.nft.details.di.NftDetailsComponent
import com.dfinn.wallet.feature_nft_impl.presentation.nft.list.di.NftListComponent
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        NftFeatureDependencies::class
    ],
    modules = [
        NftFeatureModule::class
    ]
)
@FeatureScope
interface NftFeatureComponent : NftFeatureApi {

    fun nftListComponentFactory(): NftListComponent.Factory

    fun nftDetailsComponentFactory(): NftDetailsComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance router: NftRouter,
            deps: NftFeatureDependencies
        ): NftFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            AccountFeatureApi::class,
            WalletFeatureApi::class,
            RuntimeApi::class
        ]
    )
    interface NftFeatureDependenciesComponent : NftFeatureDependencies
}
