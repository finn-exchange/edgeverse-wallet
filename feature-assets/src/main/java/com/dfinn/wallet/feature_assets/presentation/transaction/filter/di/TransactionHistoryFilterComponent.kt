package com.dfinn.wallet.feature_assets.presentation.transaction.filter.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_assets.presentation.transaction.filter.TransactionHistoryFilterFragment

@Subcomponent(
    modules = [
        TransactionHistoryFilterModule::class
    ]
)
@ScreenScope
interface TransactionHistoryFilterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): TransactionHistoryFilterComponent
    }

    fun inject(fragment: TransactionHistoryFilterFragment)
}
