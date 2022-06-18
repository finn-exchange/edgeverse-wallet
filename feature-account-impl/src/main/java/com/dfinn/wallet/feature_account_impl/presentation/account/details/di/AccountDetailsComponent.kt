package com.dfinn.wallet.feature_account_impl.presentation.account.details.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_account_impl.presentation.account.details.AccountDetailsFragment

@Subcomponent(
    modules = [
        AccountDetailsModule::class
    ]
)
@ScreenScope
interface AccountDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance metaId: Long
        ): AccountDetailsComponent
    }

    fun inject(fragment: AccountDetailsFragment)
}
