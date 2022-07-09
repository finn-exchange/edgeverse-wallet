package com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicFragment
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicPayload

@Subcomponent(
    modules = [
        ConfirmMnemonicModule::class
    ]
)
@ScreenScope
interface ConfirmMnemonicComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmMnemonicPayload
        ): ConfirmMnemonicComponent
    }

    fun inject(confirmMnemonicFragment: ConfirmMnemonicFragment)
}
