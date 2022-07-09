package com.edgeverse.wallet.feature_wallet_api.di

import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_wallet_api.data.cache.AssetCache
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

interface WalletFeatureApi {

    fun provideWalletRepository(): WalletRepository

    fun provideTokenRepository(): TokenRepository

    fun provideAssetCache(): AssetCache

    fun provideWallConstants(): WalletConstants

    @Wallet
    fun provideWalletUpdateSystem(): UpdateSystem

    fun provideFeeLoaderMixinFactory(): FeeLoaderMixin.Factory

    val assetSourceRegistry: AssetSourceRegistry

    fun provideAmountChooserFactory(): AmountChooserMixin.Factory

    val phishingValidationFactory: PhishingValidationFactory
}
