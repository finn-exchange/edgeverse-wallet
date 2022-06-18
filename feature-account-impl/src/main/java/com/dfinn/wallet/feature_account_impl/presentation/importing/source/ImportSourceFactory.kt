package com.dfinn.wallet.feature_account_impl.presentation.importing.source

import com.dfinn.wallet.common.resources.ClipboardManager
import com.dfinn.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.dfinn.wallet.feature_account_api.presenatation.account.add.SecretType
import com.dfinn.wallet.feature_account_impl.domain.account.add.AddAccountInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AdvancedEncryptionRequester
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.dfinn.wallet.feature_account_impl.presentation.importing.FileReader
import com.dfinn.wallet.feature_account_impl.presentation.importing.source.model.ImportSource
import com.dfinn.wallet.feature_account_impl.presentation.importing.source.model.JsonImportSource
import com.dfinn.wallet.feature_account_impl.presentation.importing.source.model.MnemonicImportSource
import com.dfinn.wallet.feature_account_impl.presentation.importing.source.model.RawSeedImportSource
import kotlinx.coroutines.CoroutineScope

class ImportSourceFactory(
    private val addAccountInteractor: AddAccountInteractor,
    private val clipboardManager: ClipboardManager,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val fileReader: FileReader,
) {

    fun create(
        secretType: SecretType,
        scope: CoroutineScope,
        payload: AddAccountPayload,
        accountNameChooserMixin: AccountNameChooserMixin,
    ): ImportSource {
        return when (secretType) {
            SecretType.MNEMONIC -> MnemonicImportSource(
                addAccountInteractor = addAccountInteractor,
                addAccountPayload = payload,
                advancedEncryptionInteractor = advancedEncryptionInteractor,
                advancedEncryptionCommunicator = advancedEncryptionRequester
            )
            SecretType.SEED -> RawSeedImportSource(
                addAccountInteractor = addAccountInteractor,
                addAccountPayload = payload,
                advancedEncryptionInteractor = advancedEncryptionInteractor,
                advancedEncryptionCommunicator = advancedEncryptionRequester
            )
            SecretType.JSON -> JsonImportSource(
                accountNameChooserMixin = accountNameChooserMixin,
                addAccountInteractor = addAccountInteractor,
                clipboardManager = clipboardManager,
                fileReader = fileReader,
                scope = scope,
                payload = payload
            )
        }
    }
}
