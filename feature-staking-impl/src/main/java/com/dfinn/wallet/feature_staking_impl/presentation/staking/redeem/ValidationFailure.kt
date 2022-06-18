package com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.validations.reedeem.RedeemValidationFailure

fun redeemValidationFailure(
    reason: RedeemValidationFailure,
    resourceManager: ResourceManager
): TitleAndMessage {
    return when (reason) {
        RedeemValidationFailure.CANNOT_PAY_FEES -> {
            resourceManager.getString(R.string.common_not_enough_funds_title) to
                resourceManager.getString(R.string.common_not_enough_funds_message)
        }
    }
}
