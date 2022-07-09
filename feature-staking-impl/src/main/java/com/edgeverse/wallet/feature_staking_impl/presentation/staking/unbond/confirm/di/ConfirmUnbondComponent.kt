package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload

@Subcomponent(
    modules = [
        ConfirmUnbondModule::class
    ]
)
@ScreenScope
interface ConfirmUnbondComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmUnbondPayload,
        ): ConfirmUnbondComponent
    }

    fun inject(fragment: ConfirmUnbondFragment)
}
