package com.edgeverse.wallet.feature_staking_impl.presentation.setup.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.setup.SetupStakingFragment

@Subcomponent(
    modules = [
        SetupStakingModule::class
    ]
)
@ScreenScope
interface SetupStakingComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): SetupStakingComponent
    }

    fun inject(fragment: SetupStakingFragment)
}
