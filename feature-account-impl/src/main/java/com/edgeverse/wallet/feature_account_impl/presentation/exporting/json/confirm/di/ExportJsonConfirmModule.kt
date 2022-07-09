package com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmViewModel

@Module(includes = [ViewModelModule::class])
class ExportJsonConfirmModule {

    @Provides
    @IntoMap
    @ViewModelKey(ExportJsonConfirmViewModel::class)
    fun provideViewModel(
        router: AccountRouter,
        advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
        payload: ExportJsonConfirmPayload
    ): ViewModel {
        return ExportJsonConfirmViewModel(
            router,
            advancedEncryptionCommunicator,
            payload
        )
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): ExportJsonConfirmViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ExportJsonConfirmViewModel::class.java)
    }
}
