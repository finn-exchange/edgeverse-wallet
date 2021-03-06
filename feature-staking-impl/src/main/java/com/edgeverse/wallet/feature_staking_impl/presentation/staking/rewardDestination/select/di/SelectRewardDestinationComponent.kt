package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.SelectRewardDestinationFragment

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
