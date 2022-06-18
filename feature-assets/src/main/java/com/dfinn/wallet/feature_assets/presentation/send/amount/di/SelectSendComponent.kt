package com.dfinn.wallet.feature_assets.presentation.send.amount.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_assets.presentation.AssetPayload
import com.dfinn.wallet.feature_assets.presentation.send.amount.SelectSendFragment

@Subcomponent(
    modules = [
        SelectSendModule::class
    ]
)
@ScreenScope
interface SelectSendComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance recipientAddress: String?,
            @BindsInstance payload: AssetPayload
        ): SelectSendComponent
    }

    fun inject(fragment: SelectSendFragment)
}
