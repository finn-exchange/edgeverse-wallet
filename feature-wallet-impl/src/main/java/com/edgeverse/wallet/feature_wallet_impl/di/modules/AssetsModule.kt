package com.edgeverse.wallet.feature_wallet_impl.di.modules

import dagger.Lazy
import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSource
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.TypeBasedAssetSourceRegistry

@Module(
    includes = [
        NativeAssetsModule::class,
        StatemineAssetsModule::class,
        OrmlAssetsModule::class,
        UnsupportedAssetsModule::class
    ]
)
class AssetsModule {

    @Provides
    @FeatureScope
    fun provideAssetSourceRegistry(
        @NativeAsset native: Lazy<AssetSource>,
        @StatemineAssets statemine: Lazy<AssetSource>,
        @OrmlAssets orml: Lazy<AssetSource>,
        @UnsupportedAssets unsupported: AssetSource,
    ): AssetSourceRegistry = TypeBasedAssetSourceRegistry(
        nativeSource = native,
        statemineSource = statemine,
        ormlSource = orml,
        unsupportedBalanceSource = unsupported
    )
}
