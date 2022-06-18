package com.dfinn.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.DappExtrinsicDetailsFragment

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
