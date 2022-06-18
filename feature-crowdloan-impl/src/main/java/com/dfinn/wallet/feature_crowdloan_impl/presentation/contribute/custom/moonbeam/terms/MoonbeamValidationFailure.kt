package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_crowdloan_impl.R
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsValidationFailure

fun moonbeamTermsValidationFailure(
    reason: MoonbeamTermsValidationFailure,
    resourceManager: ResourceManager
): TitleAndMessage {
    return when (reason) {
        MoonbeamTermsValidationFailure.CANNOT_PAY_FEES -> resourceManager.getString(R.string.common_not_enough_funds_title) to
            resourceManager.getString(R.string.common_not_enough_funds_message)
    }
}
