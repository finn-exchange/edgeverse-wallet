package com.edgeverse.wallet.feature_account_impl.presentation.account.list.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountListViewModel
import com.edgeverse.wallet.feature_account_impl.presentation.account.mixin.api.AccountListingMixin
import com.edgeverse.wallet.feature_account_impl.presentation.account.mixin.impl.AccountListingProvider

@Module(includes = [ViewModelModule::class])
class AccountListModule {

    @Provides
    @ScreenScope
    fun provideAccountListingMixin(
        interactor: AccountInteractor,
        addressIconGenerator: AddressIconGenerator
    ): AccountListingMixin = AccountListingProvider(interactor, addressIconGenerator)

    @Provides
    @IntoMap
    @ViewModelKey(AccountListViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        accountListingMixin: AccountListingMixin,
        accountChosenNavDirection: AccountChosenNavDirection,
    ): ViewModel {
        return AccountListViewModel(interactor, router, accountChosenNavDirection, accountListingMixin)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): AccountListViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(AccountListViewModel::class.java)
    }
}
