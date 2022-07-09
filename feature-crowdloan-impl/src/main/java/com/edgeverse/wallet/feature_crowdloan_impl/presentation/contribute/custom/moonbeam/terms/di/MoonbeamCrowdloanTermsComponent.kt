package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.MoonbeamCrowdloanTermsFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload

@Subcomponent(
    modules = [
        MoonbeamCrowdloanTermsModule::class
    ]
)
@ScreenScope
interface MoonbeamCrowdloanTermsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ContributePayload,
        ): MoonbeamCrowdloanTermsComponent
    }

    fun inject(fragment: MoonbeamCrowdloanTermsFragment)
}
