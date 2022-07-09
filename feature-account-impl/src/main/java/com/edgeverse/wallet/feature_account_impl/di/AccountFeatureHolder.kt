package com.edgeverse.wallet.feature_account_impl.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.runtime.di.RuntimeApi
import javax.inject.Inject

@ApplicationScope
class AccountFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val accountRouter: AccountRouter,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val accountFeatureDependencies = DaggerAccountFeatureComponent_AccountFeatureDependenciesComponent.builder()
            .commonApi(commonApi())
            .dbApi(getFeature(DbApi::class.java))
            .runtimeApi(getFeature(RuntimeApi::class.java))
            .build()

        return DaggerAccountFeatureComponent.factory()
            .create(
                accountRouter = accountRouter,
                advancedEncryptionCommunicator = advancedEncryptionCommunicator,
                deps = accountFeatureDependencies
            )
    }
}
