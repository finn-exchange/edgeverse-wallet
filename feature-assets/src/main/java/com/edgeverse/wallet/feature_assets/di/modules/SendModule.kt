package com.edgeverse.wallet.feature_assets.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_assets.domain.send.SendInteractor
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module
class SendModule {

    @Provides
    @FeatureScope
    fun provideSendInteractor(
        chainRegistry: ChainRegistry,
        walletRepository: WalletRepository,
        assetSourceRegistry: AssetSourceRegistry,
    ) = SendInteractor(
        chainRegistry,
        walletRepository,
        assetSourceRegistry
    )
}
