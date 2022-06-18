package com.dfinn.wallet.feature_staking_impl.presentation.payouts.detail.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.payouts.detail.PayoutDetailsFragment
import com.dfinn.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable

@Subcomponent(
    modules = [
        PayoutDetailsModule::class
    ]
)
@ScreenScope
interface PayoutDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payout: PendingPayoutParcelable
        ): PayoutDetailsComponent
    }

    fun inject(fragment: PayoutDetailsFragment)
}
