package com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMoreFragment
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload

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
