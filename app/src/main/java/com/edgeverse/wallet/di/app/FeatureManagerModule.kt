package com.edgeverse.wallet.di.app

import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.di.deps.FeatureHolderManager
import dagger.Module
import dagger.Provides

@Module
class FeatureManagerModule {

    @ApplicationScope
    @Provides
    fun provideFeatureHolderManager(featureApiHolderMap: @JvmSuppressWildcards Map<Class<*>, FeatureApiHolder>): FeatureHolderManager {
        return FeatureHolderManager(featureApiHolderMap)
    }
}
