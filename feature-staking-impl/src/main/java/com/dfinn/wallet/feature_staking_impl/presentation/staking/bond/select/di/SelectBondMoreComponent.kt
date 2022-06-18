package com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMoreFragment
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMorePayload

@Subcomponent(
    modules = [
        SelectBondMoreModule::class
    ]
)
@ScreenScope
interface SelectBondMoreComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: SelectBondMorePayload
        ): SelectBondMoreComponent
    }

    fun inject(fragment: SelectBondMoreFragment)
}
