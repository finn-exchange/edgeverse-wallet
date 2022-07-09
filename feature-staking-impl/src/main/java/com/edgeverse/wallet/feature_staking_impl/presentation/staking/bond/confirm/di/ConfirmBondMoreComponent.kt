package com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMoreFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload

@Subcomponent(
    modules = [
        ConfirmBondMoreModule::class
    ]
)
@ScreenScope
interface ConfirmBondMoreComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmBondMorePayload,
        ): ConfirmBondMoreComponent
    }

    fun inject(fragment: ConfirmBondMoreFragment)
}
