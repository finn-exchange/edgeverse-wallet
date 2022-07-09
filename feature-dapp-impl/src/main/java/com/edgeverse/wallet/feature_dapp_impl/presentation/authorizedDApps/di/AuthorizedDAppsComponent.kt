package com.edgeverse.wallet.feature_dapp_impl.presentation.authorizedDApps.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_dapp_impl.presentation.authorizedDApps.AuthorizedDAppsFragment

@Subcomponent(
    modules = [
        AuthorizedDAppsModule::class
    ]
)
@ScreenScope
interface AuthorizedDAppsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
        ): AuthorizedDAppsComponent
    }

    fun inject(fragment: AuthorizedDAppsFragment)
}
