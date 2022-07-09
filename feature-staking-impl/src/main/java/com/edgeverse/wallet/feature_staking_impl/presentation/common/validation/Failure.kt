package com.edgeverse.wallet.feature_staking_impl.presentation.common.validation

import com.edgeverse.wallet.common.base.TitleAndMessage
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount

fun stakingValidationFailure(
    payload: SetupStakingPayload,
    reason: SetupStakingValidationFailure,
    resourceManager: ResourceManager,
): TitleAndMessage {
    val (title, message) = with(resourceManager) {
        when (reason) {
            SetupStakingValidationFailure.CannotPayFee -> {
                getString(R.string.common_error_general_title) to getString(R.string.choose_amount_error_too_big)
            }

            is SetupStakingValidationFailure.TooSmallAmount -> {
                val formattedThreshold = reason.threshold.formatTokenAmount(payload.asset.token.configuration)

                getString(R.string.common_amount_low) to getString(R.string.staking_setup_amount_too_low, formattedThreshold)
            }

            SetupStakingValidationFailure.MaxNominatorsReached -> {
                getString(R.string.staking_max_nominators_reached_title) to getString(R.string.staking_max_nominators_reached_message)
            }
        }
    }

    return title to message
}
