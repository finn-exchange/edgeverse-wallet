package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.metamask.sign.MetamaskSignInteractorFactory
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.polkadotJs.sign.PolkadotJsSignInteractorFactory
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.signExtrinsic.DAppSignInteractor
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignPayload
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignViewModel
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.model.MetamaskSendTransactionRequest
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.model.PolkadotJsSignRequest
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class DAppSignModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        polkadotJsSignInteractorFactory: PolkadotJsSignInteractorFactory,
        metamaskSignInteractorFactory: MetamaskSignInteractorFactory,
        request: DAppSignPayload
    ): DAppSignInteractor = when (val body = request.body) {
        is PolkadotJsSignRequest -> polkadotJsSignInteractorFactory.create(body)
        is MetamaskSendTransactionRequest -> metamaskSignInteractorFactory.create(body)
        else -> throw IllegalArgumentException("Unknown sign request")
    }

    @Provides
    @ScreenScope
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): DAppSignViewModel {
        return ViewModelProvider(fragment, factory).get(DAppSignViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DAppSignViewModel::class)
    fun provideViewModel(
        router: DAppRouter,
        feeLoaderMixinFactory: FeeLoaderMixin.Factory,
        commonInteractor: DappInteractor,
        interactor: DAppSignInteractor,
        payload: DAppSignPayload,
        selectedAccountUseCase: SelectedAccountUseCase,
        communicator: DAppSignCommunicator,
        walletUiUseCase: WalletUiUseCase,
        validationExecutor: ValidationExecutor,
        resourceManager: ResourceManager
    ): ViewModel {
        return DAppSignViewModel(
            router = router,
            selectedAccountUseCase = selectedAccountUseCase,
            interactor = interactor,
            feeLoaderMixinFactory = feeLoaderMixinFactory,
            payload = payload,
            commonInteractor = commonInteractor,
            responder = communicator,
            walletUiUseCase = walletUiUseCase,
            validationExecutor = validationExecutor,
            resourceManager = resourceManager
        )
    }
}
