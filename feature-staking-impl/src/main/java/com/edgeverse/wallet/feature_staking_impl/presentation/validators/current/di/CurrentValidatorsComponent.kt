package com.edgeverse.wallet.feature_staking_impl.presentation.validators.current.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.current.CurrentValidatorsFragment

@Subcomponent(
    modules = [
        CurrentValidatorsModule::class
    ]
)
@ScreenScope
interface CurrentValidatorsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): CurrentValidatorsComponent
    }

    fun inject(fragment: CurrentValidatorsFragment)
}
