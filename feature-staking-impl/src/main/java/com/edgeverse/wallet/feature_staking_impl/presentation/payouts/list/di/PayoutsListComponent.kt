package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.PayoutsListFragment

@Subcomponent(
    modules = [
        PayoutsListModule::class
    ]
)
@ScreenScope
interface PayoutsListComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): PayoutsListComponent
    }

    fun inject(fragment: PayoutsListFragment)
}
