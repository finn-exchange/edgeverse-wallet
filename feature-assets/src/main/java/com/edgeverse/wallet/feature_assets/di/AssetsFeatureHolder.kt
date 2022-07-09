package com.edgeverse.wallet.feature_assets.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_nft_api.NftFeatureApi
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.runtime.di.RuntimeApi
import javax.inject.Inject

@ApplicationScope
class AssetsFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val router: WalletRouter
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val dependencies = DaggerAssetsFeatureComponent_AssetsFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .dbApi(getFeature(DbApi::class.java))
            .nftFeatureApi(getFeature(NftFeatureApi::class.java))
            .walletFeatureApi(getFeature(WalletFeatureApi::class.java))
            .runtimeApi(getFeature(RuntimeApi::class.java))
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .build()
        return DaggerAssetsFeatureComponent.factory()
            .create(router, dependencies)
    }
}
