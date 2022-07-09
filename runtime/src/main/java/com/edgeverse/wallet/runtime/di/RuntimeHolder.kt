package com.edgeverse.wallet.runtime.di

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import javax.inject.Inject

@ApplicationScope
class RuntimeHolder @Inject constructor(
    featureContainer: FeatureContainer
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val dbDependencies = DaggerRuntimeComponent_RuntimeDependenciesComponent.builder()
            .commonApi(commonApi())
            .dbApi(getFeature(DbApi::class.java))
            .build()
        return DaggerRuntimeComponent.builder()
            .runtimeDependencies(dbDependencies)
            .build()
    }
}
