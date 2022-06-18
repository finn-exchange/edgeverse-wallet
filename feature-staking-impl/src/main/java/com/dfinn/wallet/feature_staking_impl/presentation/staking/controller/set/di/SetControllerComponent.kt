package com.dfinn.wallet.feature_staking_impl.presentation.staking.controller.set.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.controller.set.SetControllerFragment

@Subcomponent(
    modules = [
        SetControllerModule::class
    ]
)
@ScreenScope
interface SetControllerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): SetControllerComponent
    }

    fun inject(fragment: SetControllerFragment)
}
