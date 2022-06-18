package com.dfinn.wallet.feature_staking_impl.presentation.staking.controller.set

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationFailure

fun bondSetControllerValidationFailure(
    reason: SetControllerValidationFailure,
    resourceManager: ResourceManager
): TitleAndMessage {
    return when (reason) {
        SetControllerValidationFailure.ALREADY_CONTROLLER -> {
            resourceManager.getString(R.string.staking_already_controller_title) to
                resourceManager.getString(R.string.staking_account_is_used_as_controller)
        }
        SetControllerValidationFailure.NOT_ENOUGH_TO_PAY_FEES -> {
            resourceManager.getString(R.string.common_not_enough_funds_title) to
                resourceManager.getString(R.string.common_not_enough_funds_message)
        }
        SetControllerValidationFailure.ZERO_CONTROLLER_BALANCE -> {
            resourceManager.getString(R.string.common_confirmation_title) to
                resourceManager.getString(R.string.staking_controller_account_zero_balance)
        }
    }
}
