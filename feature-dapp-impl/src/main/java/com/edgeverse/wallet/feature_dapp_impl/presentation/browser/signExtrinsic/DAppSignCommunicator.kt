package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic

import android.os.Parcelable
import com.edgeverse.wallet.common.navigation.InterScreenRequester
import com.edgeverse.wallet.common.navigation.InterScreenResponder
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator.Response
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

interface DAppSignRequester : InterScreenRequester<DAppSignPayload, Response>

interface DAppSignResponder : InterScreenResponder<DAppSignPayload, Response>

interface DAppSignCommunicator : DAppSignRequester, DAppSignResponder {

    sealed class Response : Parcelable {

        abstract val requestId: String

        @Parcelize
        class Rejected(override val requestId: String) : Response()

        @Parcelize
        class Signed(override val requestId: String, val signature: String) : Response()

        @Parcelize
        class Sent(override val requestId: String, val txHash: String) : Response()

        @Parcelize
        class SigningFailed(override val requestId: String) : Response()
    }
}

suspend fun DAppSignRequester.awaitConfirmation(request: DAppSignPayload): Response {
    val responsesForRequest = responseFlow.filter { it.requestId == request.body.id }

    openRequest(request)

    return responsesForRequest.first()
}
