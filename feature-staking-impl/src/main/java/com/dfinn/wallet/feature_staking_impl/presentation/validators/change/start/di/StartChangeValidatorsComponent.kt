package com.dfinn.wallet.feature_staking_impl.presentation.validators.change.start.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.start.StartChangeValidatorsFragment

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
