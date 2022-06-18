package com.dfinn.wallet.splash.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.splash.SplashRouter
import com.dfinn.wallet.splash.presentation.SplashViewModel

@Module(includes = [ViewModelModule::class])
class SplashModule {

    @Provides
    internal fun provideScannerViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): SplashViewModel {
        return ViewModelProvider(fragment, factory).get(SplashViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideSignInViewModel(accountRepository: AccountRepository, router: SplashRouter): ViewModel {
        return SplashViewModel(router, accountRepository)
    }
}
