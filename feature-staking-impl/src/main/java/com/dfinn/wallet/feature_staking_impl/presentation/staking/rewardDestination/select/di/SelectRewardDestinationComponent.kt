package com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.SelectRewardDestinationFragment

@Subcomponent(
    modules = [
        SelectRewardDestinationModule::class
    ]
)
@ScreenScope
interface SelectRewardDestinationComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): SelectRewardDestinationComponent
    }

    fun inject(fragment: SelectRewardDestinationFragment)
}
