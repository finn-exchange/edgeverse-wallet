package com.edgeverse.wallet.feature_dapp_impl.presentation.main.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_dapp_impl.presentation.main.MainDAppFragment

@Subcomponent(
    modules = [
        MainDAppModule::class
    ]
)
@ScreenScope
interface MainDAppComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): MainDAppComponent
    }

    fun inject(fragment: MainDAppFragment)
}
