package com.dfinn.wallet.feature_wallet_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_wallet_api.data.cache.AssetCache
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSource
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.StaticAssetSource
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.balances.orml.OrmlAssetBalance
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.history.orml.OrmlAssetHistory
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.orml.OrmlAssetTransfers
import com.dfinn.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.dfinn.wallet.runtime.storage.source.StorageDataSource
import javax.inject.Named
import javax.inject.Qualifier

@Qualifier
annotation class OrmlAssets

@Module
class OrmlAssetsModule {

    @Provides
    @FeatureScope
    fun provideBalance(
        chainRegistry: ChainRegistry,
        assetCache: AssetCache,
        @Named(REMOTE_STORAGE_SOURCE) storageDataSource: StorageDataSource,
    ) = OrmlAssetBalance(assetCache, storageDataSource, chainRegistry)

    @Provides
    @FeatureScope
    fun provideTransfers(
        chainRegistry: ChainRegistry,
        assetSourceRegistry: AssetSourceRegistry,
        extrinsicService: ExtrinsicService,
        phishingValidationFactory: PhishingValidationFactory,
    ) = OrmlAssetTransfers(chainRegistry, assetSourceRegistry, extrinsicService, phishingValidationFactory)

    @Provides
    @FeatureScope
    fun provideHistory(
        chainRegistry: ChainRegistry,
        eventsRepository: EventsRepository,
    ) = OrmlAssetHistory(chainRegistry, eventsRepository)

    @Provides
    @OrmlAssets
    @FeatureScope
    fun provideAssetSource(
        ormlAssetBalance: OrmlAssetBalance,
        ormlAssetTransfers: OrmlAssetTransfers,
        ormlAssetHistory: OrmlAssetHistory,
    ): AssetSource = StaticAssetSource(
        transfers = ormlAssetTransfers,
        balance = ormlAssetBalance,
        history = ormlAssetHistory
    )
}
