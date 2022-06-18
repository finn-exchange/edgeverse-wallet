package com.dfinn.wallet.feature_onboarding_impl.presentation.welcome.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.dfinn.wallet.feature_onboarding_impl.presentation.welcome.WelcomeFragment

@Subcomponent(
    modules = [
        WelcomeModule::class
    ]
)
@ScreenScope
interface WelcomeComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance shouldShowBack: Boolean,
            @BindsInstance addAccountPayload: AddAccountPayload,
        ): WelcomeComponent
    }

    fun inject(welcomeFragment: WelcomeFragment)
}
