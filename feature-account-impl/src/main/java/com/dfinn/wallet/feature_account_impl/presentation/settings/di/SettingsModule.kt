package com.dfinn.wallet.feature_account_impl.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.domain.GetAvailableFiatCurrencies
import com.dfinn.wallet.common.domain.SelectedFiat
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.settings.SettingsViewModel

@Module(includes = [ViewModelModule::class])
class SettingsModule {

    @Provides
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        appLinksProvider: AppLinksProvider,
        addressIconGenerator: AddressIconGenerator,
        getAvailableFiatCurrencies: GetAvailableFiatCurrencies,
        selectedFiat: SelectedFiat,
    ): ViewModel {
        return SettingsViewModel(
            interactor,
            router,
            appLinksProvider,
            addressIconGenerator,
            getAvailableFiatCurrencies,
            selectedFiat
        )
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): SettingsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SettingsViewModel::class.java)
    }
}
