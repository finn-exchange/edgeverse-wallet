package com.edgeverse.wallet.feature_assets.presentation.send.amount.di

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
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.addressInput.AddressInputMixinFactory
import com.edgeverse.wallet.feature_assets.domain.WalletInteractor
import com.edgeverse.wallet.feature_assets.domain.send.SendInteractor
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.send.amount.SelectSendViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class SelectSendModule {

    @Provides
    @IntoMap
    @ViewModelKey(SelectSendViewModel::class)
    fun provideViewModel(
        interactor: WalletInteractor,
        sendInteractor: SendInteractor,
        validationExecutor: ValidationExecutor,
        selectedAccountUseCase: SelectedAccountUseCase,
        router: WalletRouter,
        chainRegistry: ChainRegistry,
        feeLoaderMixinFactory: FeeLoaderMixin.Factory,
        resourceManager: ResourceManager,
        amountChooserMixinFactory: AmountChooserMixin.Factory,
        addressInputMixinFactory: AddressInputMixinFactory,
        assetPayload: AssetPayload,
        recipientAddress: String?
    ): ViewModel {
        return SelectSendViewModel(
            interactor = interactor,
            router = router,
            assetPayload = assetPayload,
            chainRegistry = chainRegistry,
            feeLoaderMixinFactory = feeLoaderMixinFactory,
            resourceManager = resourceManager,
            amountChooserMixinFactory = amountChooserMixinFactory,
            sendInteractor = sendInteractor,
            validationExecutor = validationExecutor,
            selectedAccountUseCase = selectedAccountUseCase,
            addressInputMixinFactory = addressInputMixinFactory,
            initialRecipientAddress = recipientAddress
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): SelectSendViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SelectSendViewModel::class.java)
    }
}
