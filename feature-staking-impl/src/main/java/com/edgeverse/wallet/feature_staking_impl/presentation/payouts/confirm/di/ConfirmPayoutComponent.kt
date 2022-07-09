package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.ConfirmPayoutFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload

@Subcomponent(
    modules = [
        ConfirmPayoutModule::class
    ]
)
@ScreenScope
interface ConfirmPayoutComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmPayoutPayload
        ): ConfirmPayoutComponent
    }

    fun inject(fragment: ConfirmPayoutFragment)
}
