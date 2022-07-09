package com.edgeverse.wallet.root.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.root.navigation.Navigator
import com.edgeverse.wallet.runtime.di.RuntimeApi
import javax.inject.Inject

@ApplicationScope
class RootFeatureHolder @Inject constructor(
    private val navigationHolder: NavigationHolder,
    private val navigator: Navigator,
    featureContainer: FeatureContainer
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val rootFeatureDependencies = DaggerRootComponent_RootFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .dbApi(getFeature(DbApi::class.java))
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .walletFeatureApi(getFeature(WalletFeatureApi::class.java))
            .stakingFeatureApi(getFeature(StakingFeatureApi::class.java))
            .assetsFeatureApi(getFeature(AssetsFeatureApi::class.java))
            .crowdloanFeatureApi(getFeature(CrowdloanFeatureApi::class.java))
            .runtimeApi(getFeature(RuntimeApi::class.java))
            .build()

        return DaggerRootComponent.factory()
            .create(navigationHolder, navigator, rootFeatureDependencies)
    }
}
