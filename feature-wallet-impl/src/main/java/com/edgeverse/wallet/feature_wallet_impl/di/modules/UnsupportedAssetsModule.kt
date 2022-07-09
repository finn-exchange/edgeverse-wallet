package com.edgeverse.wallet.feature_wallet_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSource
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.StaticAssetSource
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.balances.UnsupportedAssetBalance
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.history.UnsupportedAssetHistory
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.UnsupportedAssetTransfers
import javax.inject.Qualifier

@Qualifier
annotation class UnsupportedAssets

@Module
class UnsupportedAssetsModule {

    @Provides
    @FeatureScope
    fun provideBalance() = UnsupportedAssetBalance()

    @Provides
    @FeatureScope
    fun provideTransfers() = UnsupportedAssetTransfers()

    @Provides
    @FeatureScope
    fun provideHistory() = UnsupportedAssetHistory()

    @Provides
    @FeatureScope
    @UnsupportedAssets
    fun provideSource(
        unsupportedAssetBalance: UnsupportedAssetBalance,
        unsupportedAssetTransfers: UnsupportedAssetTransfers,
        unsupportedAssetHistory: UnsupportedAssetHistory,
    ): AssetSource = StaticAssetSource(
        transfers = unsupportedAssetTransfers,
        balance = unsupportedAssetBalance,
        history = unsupportedAssetHistory
    )
}
