package com.edgeverse.wallet.feature_account_impl.presentation.importing.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.edgeverse.wallet.feature_account_impl.presentation.importing.ImportAccountFragment

@Subcomponent(
    modules = [
        ImportAccountModule::class
    ]
)
@ScreenScope
interface ImportAccountComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ImportAccountPayload
        ): ImportAccountComponent
    }

    fun inject(importAccountFragment: ImportAccountFragment)
}
