package com.edgeverse.wallet.feature_assets.presentation.balance.filters.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_assets.presentation.balance.filters.AssetFiltersFragment

@Subcomponent(
    modules = [
        AssetFiltersModule::class
    ]
)
@ScreenScope
interface AssetFiltersComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
        ): AssetFiltersComponent
    }

    fun inject(fragment: AssetFiltersFragment)
}
