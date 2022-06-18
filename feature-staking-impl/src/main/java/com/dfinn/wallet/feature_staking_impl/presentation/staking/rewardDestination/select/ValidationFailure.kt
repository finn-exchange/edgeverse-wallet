package com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.select

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationFailure

fun rewardDestinationValidationFailure(
    resourceManager: ResourceManager,
    failure: RewardDestinationValidationFailure
): TitleAndMessage = when (failure) {

    is RewardDestinationValidationFailure.MissingController -> {
        resourceManager.getString(R.string.common_error_general_title) to
            resourceManager.getString(R.string.staking_add_controller, failure.controllerAddress)
    }

    RewardDestinationValidationFailure.CannotPayFees -> {
        resourceManager.getString(R.string.common_not_enough_funds_title) to
            resourceManager.getString(R.string.common_not_enough_funds_message)
    }
}
