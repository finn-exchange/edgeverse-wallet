package com.edgeverse.wallet.feature_account_impl.presentation.exporting.seed.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_account_impl.domain.account.export.seed.ExportSeedInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.ExportPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.seed.ExportSeedViewModel

@Module(includes = [ViewModelModule::class])
class ExportSeedModule {

    @Provides
    @IntoMap
    @ViewModelKey(ExportSeedViewModel::class)
    fun provideViewModel(
        router: AccountRouter,
        advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
        interactor: ExportSeedInteractor,
        payload: ExportPayload,
    ): ViewModel {
        return ExportSeedViewModel(
            router,
            interactor,
            advancedEncryptionCommunicator,
            payload
        )
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): ExportSeedViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ExportSeedViewModel::class.java)
    }
}
