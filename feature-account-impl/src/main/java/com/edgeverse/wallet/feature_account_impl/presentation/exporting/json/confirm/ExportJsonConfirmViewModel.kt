package com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm

import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionRequester
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.ExportViewModel

class ExportJsonConfirmViewModel(
    private val router: AccountRouter,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val payload: ExportJsonConfirmPayload
) : ExportViewModel() {

    val json = payload.json

    fun back() {
        router.back()
    }

    fun confirmClicked() {
        exportText(json)
    }

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(payload.exportPayload.metaId, payload.exportPayload.chainId, hideDerivationPaths = true)

        advancedEncryptionRequester.openRequest(viewRequest)
    }
}
