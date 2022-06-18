package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.CrowdloanContributeFragment
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload

@Subcomponent(
    modules = [
        CrowdloanContributeModule::class
    ]
)
@ScreenScope
interface CrowdloanContributeComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ContributePayload
        ): CrowdloanContributeComponent
    }

    fun inject(fragment: CrowdloanContributeFragment)
}
