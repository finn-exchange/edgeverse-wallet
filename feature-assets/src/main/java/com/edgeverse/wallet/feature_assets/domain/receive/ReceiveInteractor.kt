package com.edgeverse.wallet.feature_assets.domain.receive

import android.graphics.Bitmap
import android.net.Uri
import com.edgeverse.wallet.common.interfaces.FileProvider
import com.edgeverse.wallet.common.utils.write
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val QR_FILE_NAME = "share-qr-address.png"

class ReceiveInteractor(
    private val fileProvider: FileProvider,
    private val chainRegistry: ChainRegistry,
    private val accountRepository: AccountRepository,
) {

    suspend fun getQrCodeSharingString(chainId: ChainId): String = withContext(Dispatchers.Default) {
        val chain = chainRegistry.getChain(chainId)
        val account = accountRepository.getSelectedMetaAccount()

        accountRepository.createQrAccountContent(chain, account)
    }

    suspend fun generateTempQrFile(qrCode: Bitmap): Result<Uri> = withContext(Dispatchers.IO) {
        runCatching {
            val file = fileProvider.generateTempFile(fixedName = QR_FILE_NAME)
            file.write(qrCode)

            fileProvider.uriOf(file)
        }
    }
}
