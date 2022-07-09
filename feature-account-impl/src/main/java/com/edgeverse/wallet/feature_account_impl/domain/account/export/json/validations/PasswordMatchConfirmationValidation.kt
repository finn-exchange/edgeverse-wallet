package com.edgeverse.wallet.feature_account_impl.domain.account.export.json.validations

import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.validOrError

class PasswordMatchConfirmationValidation : ExportJsonPasswordValidation {

    override suspend fun validate(value: ExportJsonPasswordValidationPayload): ValidationStatus<ExportJsonPasswordValidationFailure> {
        return validOrError(value.password == value.passwordConfirmation) {
            ExportJsonPasswordValidationFailure.PASSWORDS_DO_NOT_MATCH
        }
    }
}
