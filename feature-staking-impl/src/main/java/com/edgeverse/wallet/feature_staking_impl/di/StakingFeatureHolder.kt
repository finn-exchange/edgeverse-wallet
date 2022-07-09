package com.edgeverse.wallet.feature_staking_impl.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.runtime.di.RuntimeApi
import javax.inject.Inject

@ApplicationScope
class StakingFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val router: StakingRouter
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val dependencies = DaggerStakingFeatureComponent_StakingFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .runtimeApi(getFeature(RuntimeApi::class.java))
            .dbApi(getFeature(DbApi::class.java))
            .walletFeatureApi(getFeature(WalletFeatureApi::class.java))
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .build()

        return DaggerStakingFeatureComponent.factory()
            .create(router, dependencies)
    }
}
