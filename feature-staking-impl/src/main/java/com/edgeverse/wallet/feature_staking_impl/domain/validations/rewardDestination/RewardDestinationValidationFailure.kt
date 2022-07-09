package com.edgeverse.wallet.feature_staking_impl.domain.validations.rewardDestination

sealed class RewardDestinationValidationFailure {
    object CannotPayFees : RewardDestinationValidationFailure()

    class MissingController(val controllerAddress: String) : RewardDestinationValidationFailure()
}
