package com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmFragment
import com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload

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
