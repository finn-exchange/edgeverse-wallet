package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.select.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.select.SelectUnbondFragment

@Subcomponent(
    modules = [
        SelectUnbondModule::class
    ]
)
@ScreenScope
interface SelectUnbondComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): SelectUnbondComponent
    }

    fun inject(fragment: SelectUnbondFragment)
}
