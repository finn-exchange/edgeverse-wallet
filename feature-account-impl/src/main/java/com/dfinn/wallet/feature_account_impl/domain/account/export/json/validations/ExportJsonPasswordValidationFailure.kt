package com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_impl.R

enum class ExportJsonPasswordValidationFailure {
    PASSWORDS_DO_NOT_MATCH
}

fun mapExportJsonPasswordValidationFailureToUi(
    resourceManager: ResourceManager,
    failure: ExportJsonPasswordValidationFailure,
): TitleAndMessage {

    return when (failure) {
        ExportJsonPasswordValidationFailure.PASSWORDS_DO_NOT_MATCH -> resourceManager.getString(R.string.common_error_general_title) to
            resourceManager.getString(R.string.export_json_password_match_error)
    }
}
