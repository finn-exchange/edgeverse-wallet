package com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.ConfirmRewardDestinationFragment
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.ConfirmRewardDestinationPayload

@Subcomponent(
    modules = [
        ConfirmRewardDestinationModule::class
    ]
)
@ScreenScope
interface ConfirmRewardDestinationComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmRewardDestinationPayload
        ): ConfirmRewardDestinationComponent
    }

    fun inject(fragment: ConfirmRewardDestinationFragment)
}
