package com.edgeverse.wallet.feature_account_impl.domain.account.export.json.validations

import com.edgeverse.wallet.common.base.TitleAndMessage
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_impl.R

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
