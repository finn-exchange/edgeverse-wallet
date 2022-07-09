package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload

@Subcomponent(
    modules = [
        ConfirmRebondModule::class
    ]
)
@ScreenScope
interface ConfirmRebondComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmRebondPayload,
        ): ConfirmRebondComponent
    }

    fun inject(fragment: ConfirmRebondFragment)
}
