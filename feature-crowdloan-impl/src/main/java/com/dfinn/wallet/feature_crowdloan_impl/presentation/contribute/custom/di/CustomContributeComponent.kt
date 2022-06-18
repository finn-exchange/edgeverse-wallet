package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeFragment
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload

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
