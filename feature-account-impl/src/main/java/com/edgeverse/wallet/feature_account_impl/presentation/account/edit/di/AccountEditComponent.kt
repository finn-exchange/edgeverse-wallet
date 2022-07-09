package com.edgeverse.wallet.feature_account_impl.presentation.account.edit.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_impl.presentation.account.edit.AccountEditFragment

@Subcomponent(
    modules = [
        AccountEditModule::class
    ]
)
@ScreenScope
interface AccountEditComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): AccountEditComponent
    }

    fun inject(fragment: AccountEditFragment)
}
