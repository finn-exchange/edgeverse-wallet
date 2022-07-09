package com.edgeverse.wallet.feature_wallet_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_wallet_api.data.cache.AssetCache
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSource
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.SubstrateRemoteSource
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.StaticAssetSource
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.balances.utility.NativeAssetBalance
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.history.utility.NativeAssetHistory
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.utility.NativeAssetTransfers
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import javax.inject.Qualifier

@Qualifier
annotation class NativeAsset

@Module
class NativeAssetsModule {

    @Provides
    @FeatureScope
    fun provideBalance(
        chainRegistry: ChainRegistry,
        assetCache: AssetCache,
        substrateRemoteSource: SubstrateRemoteSource,
    ) = NativeAssetBalance(chainRegistry, assetCache, substrateRemoteSource)

    @Provides
    @FeatureScope
    fun provideTransfers(
        chainRegistry: ChainRegistry,
        assetSourceRegistry: AssetSourceRegistry,
        extrinsicService: ExtrinsicService,
        phishingValidationFactory: PhishingValidationFactory,
    ) = NativeAssetTransfers(chainRegistry, assetSourceRegistry, extrinsicService, phishingValidationFactory)

    @Provides
    @FeatureScope
    fun provideHistory(
        chainRegistry: ChainRegistry,
        eventsRepository: EventsRepository,
    ) = NativeAssetHistory(chainRegistry, eventsRepository)

    @Provides
    @NativeAsset
    @FeatureScope
    fun provideAssetSource(
        nativeAssetBalance: NativeAssetBalance,
        nativeAssetTransfers: NativeAssetTransfers,
        nativeAssetHistory: NativeAssetHistory,
    ): AssetSource = StaticAssetSource(
        transfers = nativeAssetTransfers,
        balance = nativeAssetBalance,
        history = nativeAssetHistory
    )
}
