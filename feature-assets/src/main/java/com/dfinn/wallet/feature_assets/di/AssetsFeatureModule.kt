package com.dfinn.wallet.feature_assets.di

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.memory.ComputationalCache
import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_assets.BuildConfig
import com.dfinn.wallet.feature_assets.data.buyToken.BuyTokenRegistry
import com.dfinn.wallet.feature_assets.data.buyToken.providers.RampProvider
import com.dfinn.wallet.feature_assets.data.buyToken.providers.TransakProvider
import com.dfinn.wallet.feature_assets.data.repository.assetFilters.AssetFiltersRepository
import com.dfinn.wallet.feature_assets.data.repository.assetFilters.PreferencesAssetFiltersRepository
import com.dfinn.wallet.feature_assets.di.modules.SendModule
import com.dfinn.wallet.feature_assets.domain.WalletInteractor
import com.dfinn.wallet.feature_assets.domain.WalletInteractorImpl
import com.dfinn.wallet.feature_assets.presentation.balance.assetActions.buy.BuyMixinFactory
import com.dfinn.wallet.feature_assets.presentation.transaction.filter.HistoryFiltersProviderFactory
import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

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
