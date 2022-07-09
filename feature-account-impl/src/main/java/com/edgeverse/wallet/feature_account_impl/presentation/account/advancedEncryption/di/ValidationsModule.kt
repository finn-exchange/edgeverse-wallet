package com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.validation.from
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidation
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationSystem
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.EthereumDerivationPathValidation
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.SubstrateDerivationPathValidation

@Module
class ValidationsModule {

    @Provides
    @ScreenScope
    @IntoSet
    fun substrateDerivationPathValidation(): AdvancedEncryptionValidation = SubstrateDerivationPathValidation()

    @Provides
    @ScreenScope
    @IntoSet
    fun ethereumDerivationPathValidation(): AdvancedEncryptionValidation = EthereumDerivationPathValidation()

    @Provides
    @ScreenScope
    fun provideValidationSystem(
        validations: Set<@JvmSuppressWildcards AdvancedEncryptionValidation>
    ) = AdvancedEncryptionValidationSystem.from(validations)
}
