package com.edgeverse.wallet.feature_dapp_impl.web3.polkadotJs

import android.webkit.WebView
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.web3.states.ExtensionsStore
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.Web3Injector
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewScriptInjector
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewWeb3JavaScriptInterface

// should be in tact with javascript_interface_bridge.js
private const val JS_INTERFACE_NAME = "PolkadotJs"

class PolkadotJsInjector(
    private val jsInterface: WebViewWeb3JavaScriptInterface,
    private val webViewScriptInjector: WebViewScriptInjector
) : Web3Injector {

    override fun initialInject(into: WebView, extensionStore: ExtensionsStore) {
        webViewScriptInjector.injectJsInterface(into, jsInterface, JS_INTERFACE_NAME)
    }

    override fun injectForPage(into: WebView, url: String, extensionStore: ExtensionsStore) {
        webViewScriptInjector.injectScript(R.raw.polkadotjs_min, into, scriptId = "novawallet-polkadotjs-bundle")
        webViewScriptInjector.injectScript(R.raw.javascript_interface_bridge, into, scriptId = "novawallet-polkadotjs-provider")
    }
}
