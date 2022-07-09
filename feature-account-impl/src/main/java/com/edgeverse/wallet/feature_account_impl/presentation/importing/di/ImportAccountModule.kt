package com.edgeverse.wallet.feature_account_impl.presentation.importing.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.resources.ClipboardManager
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.edgeverse.wallet.feature_account_impl.domain.account.add.AddAccountInteractor
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.impl.AccountNameChooserFactory
import com.edgeverse.wallet.feature_account_impl.presentation.importing.FileReader
import com.edgeverse.wallet.feature_account_impl.presentation.importing.ImportAccountViewModel
import com.edgeverse.wallet.feature_account_impl.presentation.importing.source.ImportSourceFactory

@Module(includes = [ViewModelModule::class])
class ImportAccountModule {

    @Provides
    fun provideImportSourceFactory(
        addAccountInteractor: AddAccountInteractor,
        clipboardManager: ClipboardManager,
        advancedEncryptionRequester: AdvancedEncryptionCommunicator,
        fileReader: FileReader,
        advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    ) = ImportSourceFactory(
        addAccountInteractor = addAccountInteractor,
        clipboardManager = clipboardManager,
        advancedEncryptionInteractor = advancedEncryptionInteractor,
        advancedEncryptionRequester = advancedEncryptionRequester,
        fileReader = fileReader
    )

    @Provides
    fun provideNameChooserMixinFactory(
        payload: ImportAccountPayload,
    ): MixinFactory<AccountNameChooserMixin.Presentation> {
        return AccountNameChooserFactory(payload.addAccountPayload)
    }

    @Provides
    @ScreenScope
    fun provideFileReader(context: Context) = FileReader(context)

    @Provides
    @IntoMap
    @ViewModelKey(ImportAccountViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        resourceManager: ResourceManager,
        accountNameChooserFactory: MixinFactory<AccountNameChooserMixin.Presentation>,
        advancedEncryptionRequester: AdvancedEncryptionCommunicator,
        importSourceFactory: ImportSourceFactory,
        payload: ImportAccountPayload,
    ): ViewModel {
        return ImportAccountViewModel(
            interactor,
            router,
            resourceManager,
            accountNameChooserFactory,
            advancedEncryptionRequester,
            payload,
            importSourceFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ImportAccountViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ImportAccountViewModel::class.java)
    }
}
