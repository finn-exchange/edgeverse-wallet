package com.dfinn.wallet.feature_account_impl.presentation.exporting.json.password.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.validation.from
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations.ExportJsonPasswordValidation
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations.ExportJsonPasswordValidationSystem
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations.PasswordMatchConfirmationValidation

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
