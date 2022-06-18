package com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem.RedeemFragment
import com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem.RedeemPayload

@Subcomponent(
    modules = [
        RedeemModule::class
    ]
)
@ScreenScope
interface RedeemComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: RedeemPayload
        ): RedeemComponent
    }

    fun inject(fragment: RedeemFragment)
}
