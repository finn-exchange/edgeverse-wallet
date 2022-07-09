package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.DappExtrinsicDetailsFragment

@Subcomponent(
    modules = [
        DAppExtrinsicDetailsModule::class
    ]
)
@ScreenScope
interface DAppExtrinsicDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance extrinsicContent: String,
        ): DAppExtrinsicDetailsComponent
    }

    fun inject(fragment: DappExtrinsicDetailsFragment)
}
