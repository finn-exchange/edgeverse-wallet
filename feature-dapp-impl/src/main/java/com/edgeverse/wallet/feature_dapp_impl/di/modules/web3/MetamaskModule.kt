package com.edgeverse.wallet.feature_dapp_impl.di.modules.web3

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_dapp_impl.BuildConfig
import com.edgeverse.wallet.feature_dapp_impl.data.network.ethereum.EthereumApiFactory
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.metamask.MetamaskInteractor
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.metamask.sign.MetamaskSignInteractorFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.di.Metamask
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.states.MetamaskStateFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskInjector
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskResponder
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskTransportFactory
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewHolder
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewScriptInjector
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewWeb3JavaScriptInterface
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.runtime.di.ExtrinsicSerialization
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import okhttp3.OkHttpClient

@Module
class MetamaskModule {

    @Provides
    @Metamask
    @FeatureScope
    fun provideWeb3JavaScriptInterface() = WebViewWeb3JavaScriptInterface()

    @Provides
    @FeatureScope
    fun provideInjector(
        gson: Gson,
        webViewScriptInjector: WebViewScriptInjector,
        @Metamask web3JavaScriptInterface: WebViewWeb3JavaScriptInterface,
    ) = MetamaskInjector(
        isDebug = BuildConfig.DEBUG,
        gson = gson,
        jsInterface = web3JavaScriptInterface,
        webViewScriptInjector = webViewScriptInjector
    )

    @Provides
    @FeatureScope
    fun provideResponder(webViewHolder: WebViewHolder): MetamaskResponder {
        return MetamaskResponder(webViewHolder)
    }

    @Provides
    @FeatureScope
    fun provideTransportFactory(
        responder: MetamaskResponder,
        @Metamask web3JavaScriptInterface: WebViewWeb3JavaScriptInterface,
        gson: Gson
    ): MetamaskTransportFactory {
        return MetamaskTransportFactory(
            webViewWeb3JavaScriptInterface = web3JavaScriptInterface,
            gson = gson,
            responder = responder,
        )
    }

    @Provides
    @FeatureScope
    fun provideInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry
    ) = MetamaskInteractor(accountRepository, chainRegistry)

    @Provides
    @FeatureScope
    fun provideStateFactory(
        interactor: MetamaskInteractor,
        commonInteractor: DappInteractor,
        resourceManager: ResourceManager,
        addressIconGenerator: AddressIconGenerator,
        web3Session: Web3Session,
    ): MetamaskStateFactory {
        return MetamaskStateFactory(
            interactor = interactor,
            commonInteractor = commonInteractor,
            resourceManager = resourceManager,
            addressIconGenerator = addressIconGenerator,
            web3Session = web3Session
        )
    }

    @Provides
    @FeatureScope
    fun provideEthereumApiFactory(
        okHttpClient: OkHttpClient
    ): EthereumApiFactory {
        return EthereumApiFactory(okHttpClient)
    }

    @Provides
    @FeatureScope
    fun provideSendInteractorFactory(
        metamaskInteractor: MetamaskInteractor,
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        tokenRepository: TokenRepository,
        @ExtrinsicSerialization extrinsicGson: Gson,
        addressIconGenerator: AddressIconGenerator,
        ethereumApiFactory: EthereumApiFactory
    ) = MetamaskSignInteractorFactory(
        metamaskInteractor = metamaskInteractor,
        chainRegistry = chainRegistry,
        accountRepository = accountRepository,
        secretStoreV2 = secretStoreV2,
        tokenRepository = tokenRepository,
        extrinsicGson = extrinsicGson,
        addressIconGenerator = addressIconGenerator,
        ethereumApiFactory = ethereumApiFactory
    )
}
