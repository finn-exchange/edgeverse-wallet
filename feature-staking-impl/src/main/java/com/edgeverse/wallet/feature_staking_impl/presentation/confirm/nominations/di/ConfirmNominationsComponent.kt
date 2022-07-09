package com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations.ConfirmNominationsFragment

@Subcomponent(
    modules = [
        ConfirmNominationsModule::class
    ]
)
@ScreenScope
interface ConfirmNominationsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): ConfirmNominationsComponent
    }

    fun inject(fragment: ConfirmNominationsFragment)
}
