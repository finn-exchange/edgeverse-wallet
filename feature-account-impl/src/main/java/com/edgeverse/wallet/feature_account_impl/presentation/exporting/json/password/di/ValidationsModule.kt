package com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.password.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.validation.from
import com.edgeverse.wallet.feature_account_impl.domain.account.export.json.validations.ExportJsonPasswordValidation
import com.edgeverse.wallet.feature_account_impl.domain.account.export.json.validations.ExportJsonPasswordValidationSystem
import com.edgeverse.wallet.feature_account_impl.domain.account.export.json.validations.PasswordMatchConfirmationValidation

@Module
class ValidationsModule {

    @Provides
    @ScreenScope
    @IntoSet
    fun passwordMatchConfirmationValidation(): ExportJsonPasswordValidation = PasswordMatchConfirmationValidation()

    @Provides
    @ScreenScope
    fun provideValidationSystem(
        validations: Set<@JvmSuppressWildcards ExportJsonPasswordValidation>
    ) = ExportJsonPasswordValidationSystem.from(validations)
}
