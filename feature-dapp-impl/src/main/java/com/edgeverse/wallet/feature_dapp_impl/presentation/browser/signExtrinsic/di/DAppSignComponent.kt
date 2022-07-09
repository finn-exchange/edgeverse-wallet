package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignExtrinsicFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignPayload

@Subcomponent(
    modules = [
        DAppSignModule::class
    ]
)
@ScreenScope
interface DAppSignComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: DAppSignPayload,
        ): DAppSignComponent
    }

    fun inject(fragment: DAppSignExtrinsicFragment)
}
