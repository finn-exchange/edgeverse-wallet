package com.dfinn.wallet.feature_staking_impl.presentation.staking.main.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.main.StakingFragment

@Subcomponent(
    modules = [
        StakingModule::class
    ]
)
@ScreenScope
interface StakingComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): StakingComponent
    }

    fun inject(fragment: StakingFragment)
}
