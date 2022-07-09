package com.edgeverse.wallet.feature_account_impl.presentation.language.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.language.LanguagesViewModel

@Module(includes = [ViewModelModule::class])
class LanguagesModule {

    @Provides
    @IntoMap
    @ViewModelKey(LanguagesViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter
    ): ViewModel {
        return LanguagesViewModel(interactor, router)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): LanguagesViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(LanguagesViewModel::class.java)
    }
}
