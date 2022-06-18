package com.dfinn.wallet.feature_staking_impl.presentation.common.rewardDestination

import com.dfinn.wallet.common.address.AddressModel

sealed class RewardDestinationModel {

    object Restake : RewardDestinationModel()

    class Payout(val destination: AddressModel) : RewardDestinationModel() {

        override fun equals(other: Any?): Boolean {
            return other is Payout && other.destination.address == destination.address
        }
    }
}
