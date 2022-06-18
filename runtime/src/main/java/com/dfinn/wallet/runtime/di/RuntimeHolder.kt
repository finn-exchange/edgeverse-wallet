package com.dfinn.wallet.runtime.di

import com.dfinn.wallet.common.di.FeatureApiHolder
import com.dfinn.wallet.common.di.FeatureContainer
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.core_db.di.DbApi
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
