package com.dfinn.wallet.feature_assets.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_assets.domain.send.SendInteractor
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

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
