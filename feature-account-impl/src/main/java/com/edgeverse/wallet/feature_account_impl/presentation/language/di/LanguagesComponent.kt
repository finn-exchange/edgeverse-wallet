package com.edgeverse.wallet.feature_account_impl.presentation.language.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_impl.presentation.language.LanguagesFragment

@Subcomponent(
    modules = [
        LanguagesModule::class
    ]
)
@ScreenScope
interface LanguagesComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): LanguagesComponent
    }

    fun inject(fragment: LanguagesFragment)
}
