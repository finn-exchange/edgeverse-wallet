package com.dfinn.wallet.feature_account_impl.presentation.account.advancedEncryption.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.validation.from
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidation
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationSystem
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.EthereumDerivationPathValidation
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.SubstrateDerivationPathValidation

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
