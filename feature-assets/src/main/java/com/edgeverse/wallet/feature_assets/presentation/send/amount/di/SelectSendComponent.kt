package com.edgeverse.wallet.feature_assets.presentation.send.amount.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.send.amount.SelectSendFragment

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
