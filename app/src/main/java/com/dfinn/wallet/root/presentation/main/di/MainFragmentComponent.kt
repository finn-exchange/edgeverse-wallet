package com.dfinn.wallet.root.presentation.main.di

import androidx.fragment.app.FragmentActivity
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.root.presentation.main.MainFragment
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
