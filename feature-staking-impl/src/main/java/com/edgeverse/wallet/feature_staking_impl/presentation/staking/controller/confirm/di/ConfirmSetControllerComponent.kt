package com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerPayload

@Subcomponent(
    modules = [
        ConfirmSetControllerModule::class
    ]
)
@ScreenScope
interface ConfirmSetControllerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ConfirmSetControllerPayload,
        ): ConfirmSetControllerComponent
    }

    fun inject(fragment: ConfirmSetControllerFragment)
}
