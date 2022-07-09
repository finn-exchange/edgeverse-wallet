package com.edgeverse.wallet.feature_wallet_impl.di

import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.network.coingecko.CoingeckoApi
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.core_db.dao.AssetDao
import com.edgeverse.wallet.core_db.dao.OperationDao
import com.edgeverse.wallet.core_db.dao.PhishingAddressDao
import com.edgeverse.wallet.core_db.dao.TokenDao
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.edgeverse.wallet.feature_wallet_api.data.cache.AssetCache
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_api.di.Wallet
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserProviderFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderProviderFactory
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.SubstrateRemoteSource
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.WssSubstrateSource
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.updaters.BalancesUpdateSystem
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.updaters.balance.PaymentUpdaterFactory
import com.edgeverse.wallet.feature_wallet_impl.data.network.phishing.PhishingApi
import com.edgeverse.wallet.feature_wallet_impl.data.network.subquery.SubQueryOperationsApi
import com.edgeverse.wallet.feature_wallet_impl.data.repository.RuntimeWalletConstants
import com.edgeverse.wallet.feature_wallet_impl.data.repository.TokenRepositoryImpl
import com.edgeverse.wallet.feature_wallet_impl.data.repository.WalletRepositoryImpl
import com.edgeverse.wallet.feature_wallet_impl.data.storage.TransferCursorStorage
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class WalletFeatureModule {

    @Provides
    @FeatureScope
    fun provideSubQueryApi(networkApiCreator: NetworkApiCreator): SubQueryOperationsApi {
        return networkApiCreator.create(SubQueryOperationsApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideCoingeckoApi(networkApiCreator: NetworkApiCreator): CoingeckoApi {
        return networkApiCreator.create(CoingeckoApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideAssetCache(
        tokenDao: TokenDao,
        assetDao: AssetDao,
        accountRepository: AccountRepository,
    ): AssetCache {
        return AssetCache(tokenDao, accountRepository, assetDao)
    }

    @Provides
    @FeatureScope
    fun providePhishingApi(networkApiCreator: NetworkApiCreator): PhishingApi {
        return networkApiCreator.create(PhishingApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideSubstrateSource(
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource,
    ): SubstrateRemoteSource = WssSubstrateSource(
        remoteStorageSource,
    )

    @Provides
    @FeatureScope
    fun provideTokenRepository(
        tokenDao: TokenDao,
    ): TokenRepository = TokenRepositoryImpl(
        tokenDao
    )

    @Provides
    @FeatureScope
    fun provideCursorStorage(preferences: Preferences) = TransferCursorStorage(preferences)

    @Provides
    @FeatureScope
    fun provideWalletRepository(
        substrateSource: SubstrateRemoteSource,
        operationsDao: OperationDao,
        subQueryOperationsApi: SubQueryOperationsApi,
        httpExceptionHandler: HttpExceptionHandler,
        phishingApi: PhishingApi,
        phishingAddressDao: PhishingAddressDao,
        walletConstants: WalletConstants,
        assetCache: AssetCache,
        coingeckoApi: CoingeckoApi,
        accountRepository: AccountRepository,
        cursorStorage: TransferCursorStorage,
        chainRegistry: ChainRegistry,
        tokenDao: TokenDao,
    ): WalletRepository = WalletRepositoryImpl(
        substrateSource,
        operationsDao,
        subQueryOperationsApi,
        httpExceptionHandler,
        phishingApi,
        accountRepository,
        assetCache,
        walletConstants,
        phishingAddressDao,
        cursorStorage,
        coingeckoApi,
        chainRegistry,
        tokenDao
    )

    @Provides
    @FeatureScope
    fun providePaymentUpdaterFactory(
        operationDao: OperationDao,
        assetSourceRegistry: AssetSourceRegistry,
        accountUpdateScope: AccountUpdateScope,
    ) = PaymentUpdaterFactory(
        operationDao,
        assetSourceRegistry,
        accountUpdateScope
    )

    @Provides
    @Wallet
    @FeatureScope
    fun provideFeatureUpdaters(
        chainRegistry: ChainRegistry,
        paymentUpdaterFactory: PaymentUpdaterFactory,
        accountUpdateScope: AccountUpdateScope,
    ): UpdateSystem = BalancesUpdateSystem(
        chainRegistry,
        paymentUpdaterFactory,
        accountUpdateScope,
    )

    @Provides
    @FeatureScope
    fun provideWalletConstants(
        chainRegistry: ChainRegistry,
    ): WalletConstants = RuntimeWalletConstants(chainRegistry)

    @Provides
    @FeatureScope
    fun provideAmountChooserFactory(
        resourceManager: ResourceManager
    ): AmountChooserMixin.Factory = AmountChooserProviderFactory(resourceManager)

    @Provides
    @FeatureScope
    fun provideFeeLoaderMixinFactory(resourceManager: ResourceManager): FeeLoaderMixin.Factory {
        return FeeLoaderProviderFactory(resourceManager)
    }
}
