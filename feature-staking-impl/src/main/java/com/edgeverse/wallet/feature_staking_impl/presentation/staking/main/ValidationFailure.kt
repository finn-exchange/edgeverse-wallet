package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main

import com.edgeverse.wallet.common.base.TitleAndMessage
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.StakeActionsValidationFailure
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingValidationFailure

fun welcomeStakingValidationFailure(
    reason: WelcomeStakingValidationFailure,
    resourceManager: ResourceManager
): TitleAndMessage = with(resourceManager) {
    when (reason) {
        WelcomeStakingValidationFailure.MAX_NOMINATORS_REACHED -> {
            getString(R.string.staking_max_nominators_reached_title) to getString(R.string.staking_max_nominators_reached_message)
        }
    }
}

fun mainStakingValidationFailure(
    reason: StakeActionsValidationFailure,
    resourceManager: ResourceManager
): TitleAndMessage = with(resourceManager) {
    when (reason) {
        is StakeActionsValidationFailure.ControllerRequired -> {
            getString(R.string.common_error_general_title) to
                getString(R.string.staking_add_controller, reason.controllerAddress)
        }

        is StakeActionsValidationFailure.UnbondingRequestLimitReached -> {
            getString(R.string.staking_unbonding_limit_reached_title) to
                getString(R.string.staking_unbonding_limit_reached_message, reason.limit)
        }
        is StakeActionsValidationFailure.StashRequired -> {
            getString(R.string.common_error_general_title) to
                getString(R.string.staking_stash_missing_message, reason.stashAddress)
        }
    }
}
