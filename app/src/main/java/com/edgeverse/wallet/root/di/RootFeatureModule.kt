package com.edgeverse.wallet.root.di

import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_wallet_api.di.Wallet
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.root.domain.RootInteractor
import dagger.Module
import dagger.Provides

@Module
class RootFeatureModule {

    @Provides
    @FeatureScope
    fun provideRootInteractor(
        walletRepository: WalletRepository,
        @Wallet walletUpdateSystem: UpdateSystem,
    ): RootInteractor {
        return RootInteractor(
            walletUpdateSystem,
            walletRepository
        )
    }
}
