package com.dfinn.wallet.di.app

import com.dfinn.wallet.common.di.FeatureApiHolder
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.di.deps.FeatureHolderManager
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
