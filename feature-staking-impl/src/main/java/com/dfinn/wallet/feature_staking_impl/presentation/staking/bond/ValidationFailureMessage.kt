package com.dfinn.wallet.feature_staking_impl.presentation.staking.bond

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationFailure

fun bondMoreValidationFailure(
    reason: BondMoreValidationFailure,
    resourceManager: ResourceManager
): TitleAndMessage {
    return when (reason) {
        BondMoreValidationFailure.NOT_ENOUGH_TO_PAY_FEES -> {
            resourceManager.getString(R.string.common_not_enough_funds_title) to
                resourceManager.getString(R.string.common_not_enough_funds_message)
        }

        BondMoreValidationFailure.ZERO_BOND -> {
            resourceManager.getString(R.string.common_error_general_title) to
                resourceManager.getString(R.string.common_zero_amount_error)
        }
    }
}
