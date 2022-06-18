package com.dfinn.wallet.feature_staking_impl.presentation.confirm.nominations.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.confirm.nominations.ConfirmNominationsFragment

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
