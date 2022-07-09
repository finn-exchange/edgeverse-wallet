package com.edgeverse.wallet.feature_nft_impl.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_nft_impl.NftRouter
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.runtime.di.RuntimeApi
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
