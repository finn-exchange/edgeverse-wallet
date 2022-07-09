package com.edgeverse.wallet.feature_staking_impl.presentation.validators.details.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.details.ValidatorDetailsFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.parcel.ValidatorDetailsParcelModel

@Subcomponent(
    modules = [
        ValidatorDetailsModule::class
    ]
)
@ScreenScope
interface ValidatorDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance validator: ValidatorDetailsParcelModel
        ): ValidatorDetailsComponent
    }

    fun inject(fragment: ValidatorDetailsFragment)
}
