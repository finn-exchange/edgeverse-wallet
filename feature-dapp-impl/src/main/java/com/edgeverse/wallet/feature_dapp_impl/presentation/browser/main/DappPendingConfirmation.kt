package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main

import com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi.AuthorizeDAppPayload

class DappPendingConfirmation<A : DappPendingConfirmation.Action>(
    val onConfirm: () -> Unit,
    val onDeny: () -> Unit,
    val onCancel: () -> Unit,
    val action: A
) {

    sealed class Action {
        class Authorize(val content: AuthorizeDAppPayload) : Action()

        object AcknowledgePhishingAlert : Action()

        object CloseScreen : Action()
    }
}
