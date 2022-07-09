package com.edgeverse.wallet.feature_account_impl.presentation.account.create.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.account.create.CreateAccountViewModel
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.impl.AccountNameChooserFactory

@Module(includes = [ViewModelModule::class])
class CreateAccountModule {

    @Provides
    fun provideNameChooserMixinFactory(
        payload: AddAccountPayload,
    ): MixinFactory<AccountNameChooserMixin.Presentation> {
        return AccountNameChooserFactory(payload)
    }

    @Provides
    @IntoMap
    @ViewModelKey(CreateAccountViewModel::class)
    fun provideViewModel(
        router: AccountRouter,
        payload: AddAccountPayload,
        accountNameChooserFactory: MixinFactory<AccountNameChooserMixin.Presentation>,
    ): ViewModel {
        return CreateAccountViewModel(router, payload, accountNameChooserFactory)
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): CreateAccountViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CreateAccountViewModel::class.java)
    }
}
