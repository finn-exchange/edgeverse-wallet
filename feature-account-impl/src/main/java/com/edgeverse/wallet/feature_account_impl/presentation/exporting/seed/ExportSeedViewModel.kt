package com.edgeverse.wallet.feature_account_impl.presentation.exporting.seed

import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_account_impl.domain.account.export.seed.ExportSeedInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionRequester
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.ExportPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.ExportViewModel

class ExportSeedViewModel(
    private val router: AccountRouter,
    private val interactor: ExportSeedInteractor,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val exportPayload: ExportPayload,
) : ExportViewModel() {

    val seedFlow = flowOf {
        interactor.getAccountSeed(exportPayload.metaId, exportPayload.chainId)
    }
        .inBackground()
        .share()

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(exportPayload.metaId, exportPayload.chainId)

        advancedEncryptionRequester.openRequest(viewRequest)
    }

    fun back() {
        router.back()
    }
}
