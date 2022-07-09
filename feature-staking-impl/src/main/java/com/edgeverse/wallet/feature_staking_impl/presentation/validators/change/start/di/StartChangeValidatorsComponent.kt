package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.start.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.start.StartChangeValidatorsFragment

@Subcomponent(
    modules = [
        StartChangeValidatorsModule::class
    ]
)
@ScreenScope
interface StartChangeValidatorsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): StartChangeValidatorsComponent
    }

    fun inject(fragment: StartChangeValidatorsFragment)
}
