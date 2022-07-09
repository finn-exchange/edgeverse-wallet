package com.edgeverse.wallet.feature_assets.presentation.receive.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.receive.ReceiveFragment

@Subcomponent(
    modules = [
        ReceiveModule::class
    ]
)
@ScreenScope
interface ReceiveComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: AssetPayload,
        ): ReceiveComponent
    }

    fun inject(fragment: ReceiveFragment)
}
