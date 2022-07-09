package com.edgeverse.wallet.root.presentation.main.di

import androidx.fragment.app.FragmentActivity
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.root.presentation.main.MainFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MainFragmentModule::class
    ]
)
@ScreenScope
interface MainFragmentComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance activity: FragmentActivity
        ): MainFragmentComponent
    }

    fun inject(fragment: MainFragment)
}
