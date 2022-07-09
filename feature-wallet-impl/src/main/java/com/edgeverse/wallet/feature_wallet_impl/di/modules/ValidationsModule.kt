package com.edgeverse.wallet.feature_wallet_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory

@Module
class ValidationsModule {

    @Provides
    @FeatureScope
    fun providePhishingValidationFactory(
        walletRepository: WalletRepository
    ) = PhishingValidationFactory(walletRepository)
}
