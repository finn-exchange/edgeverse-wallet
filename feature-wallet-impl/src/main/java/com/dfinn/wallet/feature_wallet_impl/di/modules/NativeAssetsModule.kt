package com.dfinn.wallet.feature_wallet_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_wallet_api.data.cache.AssetCache
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSource
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.SubstrateRemoteSource
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.StaticAssetSource
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.balances.utility.NativeAssetBalance
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.history.utility.NativeAssetHistory
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.utility.NativeAssetTransfers
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
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
