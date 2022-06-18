package com.dfinn.wallet.feature_crowdloan_impl.presentation.contributions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contributions.UserContributionsFragment

@Subcomponent(
    modules = [
        UserContributionsModule::class
    ]
)
@ScreenScope
interface UserContributionsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
        ): UserContributionsComponent
    }

    fun inject(fragment: UserContributionsFragment)
}
