package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.select.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.select.SelectCustomValidatorsFragment

@Subcomponent(
    modules = [
        SelectCustomValidatorsModule::class
    ]
)
@ScreenScope
interface SelectCustomValidatorsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): SelectCustomValidatorsComponent
    }

    fun inject(fragment: SelectCustomValidatorsFragment)
}
