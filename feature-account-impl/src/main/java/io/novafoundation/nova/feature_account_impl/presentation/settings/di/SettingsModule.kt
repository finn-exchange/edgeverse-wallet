package io.novafoundation.nova.feature_account_impl.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.data.network.AppLinksProvider
import io.novafoundation.nova.common.di.viewmodel.ViewModelKey
import io.novafoundation.nova.common.di.viewmodel.ViewModelModule
import io.novafoundation.nova.common.domain.GetAvailableFiatCurrencies
import io.novafoundation.nova.common.domain.SelectedFiat
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.settings.SettingsViewModel

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
