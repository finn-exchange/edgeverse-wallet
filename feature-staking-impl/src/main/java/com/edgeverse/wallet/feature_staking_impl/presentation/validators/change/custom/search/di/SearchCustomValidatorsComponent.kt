package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.search.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.search.SearchCustomValidatorsFragment

@Subcomponent(
    modules = [
        SearchCustomValidatorsModule::class
    ]
)
@ScreenScope
interface SearchCustomValidatorsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance fragment: Fragment): SearchCustomValidatorsComponent
    }

    fun inject(fragment: SearchCustomValidatorsFragment)
}
