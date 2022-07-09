package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload

@Subcomponent(
    modules = [
        CustomContributeModule::class
    ]
)
@ScreenScope
interface CustomContributeComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: CustomContributePayload
        ): CustomContributeComponent
    }

    fun inject(fragment: CustomContributeFragment)
}
