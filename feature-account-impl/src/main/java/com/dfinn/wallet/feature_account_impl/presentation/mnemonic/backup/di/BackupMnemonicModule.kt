package com.dfinn.wallet.feature_account_impl.presentation.mnemonic.backup.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.dfinn.wallet.feature_account_impl.presentation.mnemonic.backup.BackupMnemonicPayload
import com.dfinn.wallet.feature_account_impl.presentation.mnemonic.backup.BackupMnemonicViewModel

@Module(includes = [ViewModelModule::class])
class BackupMnemonicModule {

    @Provides
    @IntoMap
    @ViewModelKey(BackupMnemonicViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        exportMnemonicInteractor: ExportMnemonicInteractor,
        payload: BackupMnemonicPayload,
        resourceManager: ResourceManager,
        advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
        advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    ): ViewModel {
        return BackupMnemonicViewModel(
            interactor,
            exportMnemonicInteractor,
            router,
            payload,
            advancedEncryptionInteractor,
            resourceManager,
            advancedEncryptionCommunicator,
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): BackupMnemonicViewModel {
        return ViewModelProvider(
            fragment,
            viewModelFactory
        ).get(BackupMnemonicViewModel::class.java)
    }
}
