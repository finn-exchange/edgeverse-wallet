package com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationSystem
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionViewModel

@Module(includes = [ViewModelModule::class, ValidationsModule::class])
class AdvancedEncryptionModule {

    @Provides
    @IntoMap
    @ViewModelKey(AdvancedEncryptionViewModel::class)
    fun provideViewModel(
        router: AccountRouter,
        payload: AdvancedEncryptionPayload,
        interactor: AdvancedEncryptionInteractor,
        resourceManager: ResourceManager,
        validationSystem: AdvancedEncryptionValidationSystem,
        validationExecutor: ValidationExecutor,
        communicator: AdvancedEncryptionCommunicator,
    ): ViewModel {
        return AdvancedEncryptionViewModel(router, payload, interactor, resourceManager, validationSystem, validationExecutor, communicator)
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): AdvancedEncryptionViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(AdvancedEncryptionViewModel::class.java)
    }
}
