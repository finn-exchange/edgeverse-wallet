package com.dfinn.wallet.feature_staking_impl.presentation.staking.rebond.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondFragment
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload

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
