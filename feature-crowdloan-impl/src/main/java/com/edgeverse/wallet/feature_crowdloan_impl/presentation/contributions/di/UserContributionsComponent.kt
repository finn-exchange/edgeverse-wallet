package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions.UserContributionsFragment

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
