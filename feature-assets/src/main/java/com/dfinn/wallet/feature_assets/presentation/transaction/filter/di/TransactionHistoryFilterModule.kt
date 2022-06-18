package com.dfinn.wallet.feature_assets.presentation.transaction.filter.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.transaction.filter.HistoryFiltersProviderFactory
import com.dfinn.wallet.feature_assets.presentation.transaction.filter.TransactionHistoryFilterViewModel

@Module(includes = [ViewModelModule::class])
class TransactionHistoryFilterModule {

    @Provides
    @IntoMap
    @ViewModelKey(TransactionHistoryFilterViewModel::class)
    fun provideViewModel(
        router: WalletRouter,
        historyFiltersProviderFactory: HistoryFiltersProviderFactory
    ): ViewModel {
        return TransactionHistoryFilterViewModel(router, historyFiltersProviderFactory)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): TransactionHistoryFilterViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(TransactionHistoryFilterViewModel::class.java)
    }
}
