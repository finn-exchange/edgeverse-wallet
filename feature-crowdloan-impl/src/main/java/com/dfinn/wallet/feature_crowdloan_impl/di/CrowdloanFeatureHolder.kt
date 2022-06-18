package com.dfinn.wallet.feature_crowdloan_impl.di

import com.dfinn.wallet.common.di.FeatureApiHolder
import com.dfinn.wallet.common.di.FeatureContainer
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.runtime.di.RuntimeApi
import javax.inject.Inject

@ApplicationScope
class CrowdloanFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val router: CrowdloanRouter
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val dependencies = DaggerCrowdloanFeatureComponent_CrowdloanFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .runtimeApi(getFeature(RuntimeApi::class.java))
            .dbApi(getFeature(DbApi::class.java))
            .walletFeatureApi(getFeature(WalletFeatureApi::class.java))
            .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
            .build()

        return DaggerCrowdloanFeatureComponent.factory()
            .create(router, dependencies)
    }
}
