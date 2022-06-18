package com.dfinn.wallet.feature_account_impl.presentation.account.list.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import com.dfinn.wallet.feature_account_impl.presentation.account.list.AccountListFragment

@Subcomponent(
    modules = [
        AccountListModule::class
    ]
)
@ScreenScope
interface AccountListComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance accountChosenNavDirection: AccountChosenNavDirection
        ): AccountListComponent
    }

    fun inject(fragment: AccountListFragment)
}
