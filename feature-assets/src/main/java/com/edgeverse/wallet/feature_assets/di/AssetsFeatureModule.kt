package com.edgeverse.wallet.feature_assets.di

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.memory.ComputationalCache
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_assets.BuildConfig
import com.edgeverse.wallet.feature_assets.data.buyToken.BuyTokenRegistry
import com.edgeverse.wallet.feature_assets.data.buyToken.providers.RampProvider
import com.edgeverse.wallet.feature_assets.data.buyToken.providers.TransakProvider
import com.edgeverse.wallet.feature_assets.data.repository.assetFilters.AssetFiltersRepository
import com.edgeverse.wallet.feature_assets.data.repository.assetFilters.PreferencesAssetFiltersRepository
import com.edgeverse.wallet.feature_assets.di.modules.SendModule
import com.edgeverse.wallet.feature_assets.domain.WalletInteractor
import com.edgeverse.wallet.feature_assets.domain.WalletInteractorImpl
import com.edgeverse.wallet.feature_assets.presentation.balance.assetActions.buy.BuyMixinFactory
import com.edgeverse.wallet.feature_assets.presentation.transaction.filter.HistoryFiltersProviderFactory
import com.edgeverse.wallet.feature_nft_api.data.repository.NftRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [SendModule::class])
class AssetsFeatureModule {

    @Provides
    @FeatureScope
    fun provideAssetFiltersRepository(preferences: Preferences): AssetFiltersRepository {
        return PreferencesAssetFiltersRepository(preferences)
    }

    @Provides
    @FeatureScope
    fun provideWalletInteractor(
        walletRepository: WalletRepository,
        accountRepository: AccountRepository,
        assetFiltersRepository: AssetFiltersRepository,
        chainRegistry: ChainRegistry,
        nftRepository: NftRepository,
    ): WalletInteractor = WalletInteractorImpl(
        walletRepository,
        accountRepository,
        assetFiltersRepository,
        chainRegistry,
        nftRepository
    )

    @Provides
    @FeatureScope
    fun provideTransakProvider(): TransakProvider {
        val environment = if (BuildConfig.DEBUG) "STAGING" else "PRODUCTION"

        return TransakProvider(
            host = BuildConfig.TRANSAK_HOST,
            apiKey = BuildConfig.TRANSAK_TOKEN,
            environment = environment
        )
    }

    @Provides
    @FeatureScope
    fun provideRampProvider(): RampProvider {
        return RampProvider(
            host = BuildConfig.RAMP_HOST,
            apiToken = BuildConfig.RAMP_TOKEN,
        )
    }

    @Provides
    @FeatureScope
    fun provideBuyTokenIntegration(
        rampProvider: RampProvider,
        transakProvider: TransakProvider
    ): BuyTokenRegistry {

        return BuyTokenRegistry(
            providers = listOf(
                rampProvider,
                transakProvider,
                // TODO waiting for secret keys for Moonpay
//                MoonPayProvider(host = BuildConfig.MOONPAY_HOST, publicKey = BuildConfig.MOONPAY_PUBLIC_KEY, privateKey = BuildConfig.MOONPAY_PRIVATE_KEY)
            )
        )
    }

    @Provides
    fun provideBuyMixinFactory(
        buyTokenRegistry: BuyTokenRegistry,
        chainRegistry: ChainRegistry,
        accountUseCase: SelectedAccountUseCase,
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory,
    ): BuyMixinFactory = BuyMixinFactory(
        buyTokenRegistry = buyTokenRegistry,
        chainRegistry = chainRegistry,
        accountUseCase = accountUseCase,
        awaitableMixinFactory = actionAwaitableMixinFactory
    )

    @Provides
    @FeatureScope
    fun provideHistoryFiltersProviderFactory(
        computationalCache: ComputationalCache
    ) = HistoryFiltersProviderFactory(computationalCache)
}
