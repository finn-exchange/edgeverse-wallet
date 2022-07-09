package com.edgeverse.wallet.feature_dapp_impl.di.modules.web3

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.polkadotJs.PolkadotJsExtensionInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.polkadotJs.sign.PolkadotJsSignInteractorFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsInjector
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsResponder
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsTransportFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.di.PolkadotJs
import com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs.states.PolkadotJsStateFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewHolder
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewScriptInjector
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewWeb3JavaScriptInterface
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.runtime.di.ExtrinsicSerialization
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository

@Module
class PolkadotJsModule {

    @Provides
    @PolkadotJs
    @FeatureScope
    fun provideWeb3JavaScriptInterface() = WebViewWeb3JavaScriptInterface()

    @Provides
    @FeatureScope
    fun providePolkadotJsInjector(
        webViewScriptInjector: WebViewScriptInjector,
        @PolkadotJs web3JavaScriptInterface: WebViewWeb3JavaScriptInterface,
    ) = PolkadotJsInjector(web3JavaScriptInterface, webViewScriptInjector)

    @Provides
    @FeatureScope
    fun provideResponder(webViewHolder: WebViewHolder): PolkadotJsResponder {
        return PolkadotJsResponder(webViewHolder)
    }

    @Provides
    @FeatureScope
    fun providePolkadotJsTransportFactory(
        web3Responder: PolkadotJsResponder,
        @PolkadotJs web3JavaScriptInterface: WebViewWeb3JavaScriptInterface,
        gson: Gson
    ): PolkadotJsTransportFactory {
        return PolkadotJsTransportFactory(
            webViewWeb3JavaScriptInterface = web3JavaScriptInterface,
            gson = gson,
            web3Responder = web3Responder,
        )
    }

    @Provides
    @FeatureScope
    fun providePolkadotJsInteractor(
        chainRegistry: ChainRegistry,
        runtimeVersionsRepository: RuntimeVersionsRepository,
        accountRepository: AccountRepository
    ) = PolkadotJsExtensionInteractor(chainRegistry, accountRepository, runtimeVersionsRepository)

    @Provides
    @FeatureScope
    fun providePolkadotJsStateFactory(
        interactor: PolkadotJsExtensionInteractor,
        commonInteractor: DappInteractor,
        resourceManager: ResourceManager,
        addressIconGenerator: AddressIconGenerator,
        web3Session: Web3Session
    ): PolkadotJsStateFactory {
        return PolkadotJsStateFactory(
            interactor = interactor,
            commonInteractor = commonInteractor,
            resourceManager = resourceManager,
            addressIconGenerator = addressIconGenerator,
            web3Session = web3Session,
        )
    }

    @Provides
    @FeatureScope
    fun provideSendInteractorFactory(
        extrinsicService: ExtrinsicService,
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        tokenRepository: TokenRepository,
        @ExtrinsicSerialization extrinsicGson: Gson,
        addressIconGenerator: AddressIconGenerator,
        walletRepository: WalletRepository
    ) = PolkadotJsSignInteractorFactory(
        extrinsicService = extrinsicService,
        chainRegistry = chainRegistry,
        accountRepository = accountRepository,
        secretStoreV2 = secretStoreV2,
        tokenRepository = tokenRepository,
        extrinsicGson = extrinsicGson,
        addressIconGenerator = addressIconGenerator,
        walletRepository = walletRepository
    )
}
