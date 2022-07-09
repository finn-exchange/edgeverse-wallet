package com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmFragment
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload

@Subcomponent(
    modules = [
        ExportJsonConfirmModule::class
    ]
)
@ScreenScope
interface ExportJsonConfirmComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: ExportJsonConfirmPayload
        ): ExportJsonConfirmComponent
    }

    fun inject(fragment: ExportJsonConfirmFragment)
}
