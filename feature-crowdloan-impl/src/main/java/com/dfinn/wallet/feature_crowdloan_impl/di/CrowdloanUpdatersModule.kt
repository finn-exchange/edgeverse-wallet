package com.dfinn.wallet.feature_crowdloan_impl.di

import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core.storage.StorageCache
import com.dfinn.wallet.core.updater.UpdateSystem
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.data.network.blockhain.updaters.BlockNumberUpdater
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.network.updaters.SingleChainUpdateSystem
import dagger.Module
import dagger.Provides

@Module
class CrowdloanUpdatersModule {

    @Provides
    @FeatureScope
    fun provideBlockNumberUpdater(
        chainRegistry: ChainRegistry,
        crowdloanSharedState: CrowdloanSharedState,
        storageCache: StorageCache,
    ) = BlockNumberUpdater(chainRegistry, crowdloanSharedState, storageCache)

    @Provides
    @FeatureScope
    fun provideCrowdloanUpdateSystem(
        chainRegistry: ChainRegistry,
        crowdloanSharedState: CrowdloanSharedState,
        blockNumberUpdater: BlockNumberUpdater,
    ): UpdateSystem = SingleChainUpdateSystem(
        updaters = listOf(
            blockNumberUpdater
        ),
        chainRegistry = chainRegistry,
        singleAssetSharedState = crowdloanSharedState
    )
}
