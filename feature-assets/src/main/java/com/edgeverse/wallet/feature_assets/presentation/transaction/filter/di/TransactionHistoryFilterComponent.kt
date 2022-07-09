package com.edgeverse.wallet.feature_assets.presentation.transaction.filter.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_assets.presentation.transaction.filter.TransactionHistoryFilterFragment

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
