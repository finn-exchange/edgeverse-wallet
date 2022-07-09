package com.edgeverse.wallet.feature_account_impl.presentation.account.create.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_impl.presentation.account.create.CreateAccountFragment

@Subcomponent(
    modules = [
        CreateAccountModule::class
    ]
)
@ScreenScope
interface CreateAccountComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: AddAccountPayload
        ): CreateAccountComponent
    }

    fun inject(createAccountFragment: CreateAccountFragment)
}
