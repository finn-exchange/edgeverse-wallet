package com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem.RedeemFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem.RedeemPayload

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
