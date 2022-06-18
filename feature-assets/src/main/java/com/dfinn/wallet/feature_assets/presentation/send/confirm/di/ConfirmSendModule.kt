package com.dfinn.wallet.feature_assets.presentation.send.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_assets.domain.WalletInteractor
import com.dfinn.wallet.feature_assets.domain.send.SendInteractor
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.send.TransferDraft
import com.dfinn.wallet.feature_assets.presentation.send.confirm.ConfirmSendViewModel
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class ConfirmSendModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmSendViewModel::class)
    fun provideViewModel(
        interactor: WalletInteractor,
        sendInteractor: SendInteractor,
        validationExecutor: ValidationExecutor,
        router: WalletRouter,
        addressIconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        selectedAccountUseCase: SelectedAccountUseCase,
        addressDisplayUseCase: AddressDisplayUseCase,
        feeLoaderMixinFactory: FeeLoaderMixin.Factory,
        resourceManager: ResourceManager,
        transferDraft: TransferDraft,
        chainRegistry: ChainRegistry,
        walletUiUseCase: WalletUiUseCase,
    ): ViewModel {
        return ConfirmSendViewModel(
            interactor = interactor,
            sendInteractor = sendInteractor,
            router = router,
            addressIconGenerator = addressIconGenerator,
            externalActions = externalActions,
            chainRegistry = chainRegistry,
            selectedAccountUseCase = selectedAccountUseCase,
            addressDisplayUseCase = addressDisplayUseCase,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            walletUiUseCase = walletUiUseCase,
            feeLoaderMixinFactory = feeLoaderMixinFactory,
            transferDraft = transferDraft
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ConfirmSendViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmSendViewModel::class.java)
    }
}
