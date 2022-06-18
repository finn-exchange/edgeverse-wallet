package com.dfinn.wallet.feature_wallet_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory

@Module
class ValidationsModule {

    @Provides
    @FeatureScope
    fun providePhishingValidationFactory(
        walletRepository: WalletRepository
    ) = PhishingValidationFactory(walletRepository)
}
