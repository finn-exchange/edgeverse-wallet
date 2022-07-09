package com.edgeverse.wallet.feature_assets.presentation.receive.di

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
import com.edgeverse.wallet.common.interfaces.FileProvider
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.QrCodeGenerator
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_assets.domain.receive.ReceiveInteractor
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.receive.ReceiveViewModel
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class ReceiveModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        fileProvider: FileProvider,
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
    ) = ReceiveInteractor(fileProvider, chainRegistry, accountRepository)

    @Provides
    @IntoMap
    @ViewModelKey(ReceiveViewModel::class)
    fun provideViewModel(
        interactor: ReceiveInteractor,
        qrCodeGenerator: QrCodeGenerator,
        addressIconGenerator: AddressIconGenerator,
        resourceManager: ResourceManager,
        externalActions: ExternalActions.Presentation,
        router: WalletRouter,
        chainRegistry: ChainRegistry,
        selectedAccountUseCase: SelectedAccountUseCase,
        payload: AssetPayload,
    ): ViewModel {
        return ReceiveViewModel(
            interactor,
            qrCodeGenerator,
            addressIconGenerator,
            resourceManager,
            externalActions,
            payload,
            chainRegistry,
            selectedAccountUseCase,
            router
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ReceiveViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ReceiveViewModel::class.java)
    }
}
