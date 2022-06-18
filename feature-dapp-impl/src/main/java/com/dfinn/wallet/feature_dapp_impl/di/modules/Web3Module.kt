package com.dfinn.wallet.feature_dapp_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.core_db.dao.DappAuthorizationDao
import com.dfinn.wallet.feature_dapp_impl.di.modules.web3.MetamaskModule
import com.dfinn.wallet.feature_dapp_impl.di.modules.web3.PolkadotJsModule
import com.dfinn.wallet.feature_dapp_impl.web3.metamask.states.MetamaskStateFactory
import com.dfinn.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskInjector
import com.dfinn.wallet.feature_dapp_impl.web3.metamask.transport.MetamaskTransportFactory
import com.dfinn.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsInjector
import com.dfinn.wallet.feature_dapp_impl.web3.polkadotJs.PolkadotJsTransportFactory
import com.dfinn.wallet.feature_dapp_impl.web3.polkadotJs.states.PolkadotJsStateFactory
import com.dfinn.wallet.feature_dapp_impl.web3.session.DbWeb3Session
import com.dfinn.wallet.feature_dapp_impl.web3.session.Web3Session
import com.dfinn.wallet.feature_dapp_impl.web3.states.ExtensionStoreFactory
import com.dfinn.wallet.feature_dapp_impl.web3.webview.Web3WebViewClientFactory
import com.dfinn.wallet.feature_dapp_impl.web3.webview.WebViewHolder
import com.dfinn.wallet.feature_dapp_impl.web3.webview.WebViewScriptInjector

@Module(includes = [PolkadotJsModule::class, MetamaskModule::class])
class Web3Module {

    @Provides
    @FeatureScope
    fun provideWebViewHolder() = WebViewHolder()

    @Provides
    @FeatureScope
    fun provideScriptInjector(
        resourceManager: ResourceManager,
    ) = WebViewScriptInjector(resourceManager)

    @Provides
    @FeatureScope
    fun provideWeb3ClientFactory(
        polkadotJsInjector: PolkadotJsInjector,
        metamaskInjector: MetamaskInjector,
    ) = Web3WebViewClientFactory(
        injectors = listOf(
            polkadotJsInjector,
            metamaskInjector
        )
    )

    @Provides
    @FeatureScope
    fun provideWeb3Session(
        dappAuthorizationDao: DappAuthorizationDao
    ): Web3Session = DbWeb3Session(dappAuthorizationDao)

    @Provides
    @FeatureScope
    fun provideExtensionStoreFactory(
        polkadotJsStateFactory: PolkadotJsStateFactory,
        polkadotJsTransportFactory: PolkadotJsTransportFactory,
        metamaskStateFactory: MetamaskStateFactory,
        metamaskTransportFactory: MetamaskTransportFactory,
    ) = ExtensionStoreFactory(
        polkadotJsStateFactory = polkadotJsStateFactory,
        polkadotJsTransportFactory = polkadotJsTransportFactory,
        metamaskStateFactory = metamaskStateFactory,
        metamaskTransportFactory = metamaskTransportFactory
    )
}
