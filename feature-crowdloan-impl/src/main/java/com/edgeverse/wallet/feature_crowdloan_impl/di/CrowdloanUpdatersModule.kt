package com.edgeverse.wallet.feature_crowdloan_impl.di

import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.blockhain.updaters.BlockNumberUpdater
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleChainUpdateSystem
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
