package com.edgeverse.wallet.feature_account_impl.presentation.settings.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_impl.presentation.settings.SettingsFragment

@Subcomponent(
    modules = [
        SettingsModule::class
    ]
)
@ScreenScope
interface SettingsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
        ): SettingsComponent
    }

    fun inject(settingsFragment: SettingsFragment)
}
