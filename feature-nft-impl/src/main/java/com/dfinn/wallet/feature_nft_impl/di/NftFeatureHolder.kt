package com.dfinn.wallet.feature_nft_impl.di

import com.dfinn.wallet.common.di.FeatureApiHolder
import com.dfinn.wallet.common.di.FeatureContainer
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_nft_impl.NftRouter
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.runtime.di.RuntimeApi
import javax.inject.Inject

@ApplicationScope
class NftFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val router: NftRouter,
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val dApp = DaggerNftFeatureComponent_NftFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .dbApi(getFeature(DbApi::class.java))
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .walletFeatureApi(getFeature(WalletFeatureApi::class.java))
            .runtimeApi(getFeature(RuntimeApi::class.java))
            .build()

        return DaggerNftFeatureComponent.factory()
            .create(router, dApp)
    }
}
