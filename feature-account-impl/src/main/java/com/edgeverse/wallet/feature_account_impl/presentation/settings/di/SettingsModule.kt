package com.edgeverse.wallet.feature_account_impl.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.domain.GetAvailableFiatCurrencies
import com.edgeverse.wallet.common.domain.SelectedFiat
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.settings.SettingsViewModel

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
