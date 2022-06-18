package com.dfinn.wallet.root.di

import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core.updater.UpdateSystem
import com.dfinn.wallet.feature_wallet_api.di.Wallet
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.root.domain.RootInteractor
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
