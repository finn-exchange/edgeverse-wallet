package com.edgeverse.wallet.feature_dapp_impl.web3.metamask.transport

import android.util.Log
import com.edgeverse.wallet.common.utils.LOG_TAG
import com.edgeverse.wallet.common.utils.fromJson
import com.edgeverse.wallet.common.utils.fromParsedHierarchy
import com.edgeverse.wallet.feature_dapp_impl.web3.metamask.model.*
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewWeb3JavaScriptInterface
import com.edgeverse.wallet.feature_dapp_impl.web3.webview.WebViewWeb3Transport
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope

class MetamaskTransportFactory(
    private val responder: MetamaskResponder,
    private val webViewWeb3JavaScriptInterface: WebViewWeb3JavaScriptInterface,
    private val gson: Gson,
) {

    fun create(scope: CoroutineScope): MetamaskTransport {
        return MetamaskTransport(
            webViewWeb3JavaScriptInterface = webViewWeb3JavaScriptInterface,
            scope = scope,
            gson = gson,
            responder = responder,
        )
    }
}

private class MetamaskRequest(
    val id: String,
    @SerializedName("name")
    val identifier: String,
    @SerializedName("object")
    val payload: Any?
)

class MetamaskTransport(
    private val gson: Gson,
    private val responder: MetamaskResponder,
    webViewWeb3JavaScriptInterface: WebViewWeb3JavaScriptInterface,
    scope: CoroutineScope,
) : WebViewWeb3Transport<MetamaskTransportRequest<*>>(scope, webViewWeb3JavaScriptInterface) {

    override suspend fun messageToRequest(message: String): MetamaskTransportRequest<*>? = runCatching {
        val request = gson.fromJson<MetamaskRequest>(message)

        when (request.identifier) {
            MetamaskTransportRequest.Identifier.REQUEST_ACCOUNTS.id -> {
                MetamaskTransportRequest.RequestAccounts(request.id, gson, responder)
            }
            MetamaskTransportRequest.Identifier.ADD_ETHEREUM_CHAIN.id -> {
                val chain = gson.fromParsedHierarchy<MetamaskChain>(request.payload)

                MetamaskTransportRequest.AddEthereumChain(request.id, gson, responder, chain)
            }
            MetamaskTransportRequest.Identifier.SWITCH_ETHEREUM_CHAIN.id -> {
                val switchChainRequest = gson.fromParsedHierarchy<SwitchChainRequest>(request.payload)

                MetamaskTransportRequest.SwitchEthereumChain(request.id, gson, responder, switchChainRequest.chainId)
            }
            MetamaskTransportRequest.Identifier.SIGN_TRANSACTION.id -> {
                val transaction = gson.fromParsedHierarchy<MetamaskTransaction>(request.payload)

                MetamaskTransportRequest.SendTransaction(request.id, gson, responder, transaction)
            }
            MetamaskTransportRequest.Identifier.SIGN_TYPED_MESSAGE.id -> {
                val typedMessage = gson.fromParsedHierarchy<TypedMessage>(request.payload)

                MetamaskTransportRequest.SignTypedMessage(request.id, gson, responder, typedMessage)
            }
            MetamaskTransportRequest.Identifier.PERSONAL_SIGN.id -> {
                val personalSignMessage = gson.fromParsedHierarchy<PersonalSignMessage>(request.payload)

                MetamaskTransportRequest.PersonalSign(request.id, gson, responder, personalSignMessage)
            }
            else -> null
        }
    }
        .onFailure { Log.e(LOG_TAG, "Failed to parse dApp message: $message", it) }
        .getOrNull()
}
