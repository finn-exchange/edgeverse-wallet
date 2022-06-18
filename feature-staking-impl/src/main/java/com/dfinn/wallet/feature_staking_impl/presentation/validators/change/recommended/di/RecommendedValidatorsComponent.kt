package com.dfinn.wallet.feature_staking_impl.presentation.validators.change.recommended.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.recommended.RecommendedValidatorsFragment

@Subcomponent(
    modules = [
        RecommendedValidatorsModule::class
    ]
)
@ScreenScope
interface RecommendedValidatorsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): RecommendedValidatorsComponent
    }

    fun inject(fragment: RecommendedValidatorsFragment)
}
