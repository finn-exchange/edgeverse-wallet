package com.dfinn.wallet.feature_staking_impl.presentation.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.confirm.ConfirmStakingFragment

@Subcomponent(
    modules = [
        ConfirmStakingModule::class
    ]
)
@ScreenScope
interface ConfirmStakingComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): ConfirmStakingComponent
    }

    fun inject(fragment: ConfirmStakingFragment)
}
