package com.dfinn.wallet.feature_assets.presentation.send.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_assets.presentation.send.TransferDraft
import com.dfinn.wallet.feature_assets.presentation.send.confirm.ConfirmSendFragment

@Subcomponent(
    modules = [
        ConfirmSendModule::class
    ]
)
@ScreenScope
interface ConfirmSendComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance transferDraft: TransferDraft
        ): ConfirmSendComponent
    }

    fun inject(fragment: ConfirmSendFragment)
}
