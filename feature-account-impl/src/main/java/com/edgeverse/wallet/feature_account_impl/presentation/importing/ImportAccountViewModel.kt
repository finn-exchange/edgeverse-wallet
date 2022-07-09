package com.edgeverse.wallet.feature_account_impl.presentation.importing

import android.content.Intent
import androidx.lifecycle.asFlow
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.withFlagSet
import com.edgeverse.wallet.common.view.ButtonState
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountAlreadyExistsException
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.data.mappers.mapAddAccountPayloadToAddAccountType
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionRequester
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.WithAccountNameChooserMixin
import com.edgeverse.wallet.feature_account_impl.presentation.importing.source.ImportSourceFactory
import com.edgeverse.wallet.feature_account_impl.presentation.importing.source.model.FileRequester
import com.edgeverse.wallet.feature_account_impl.presentation.importing.source.model.ImportError
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ImportAccountViewModel(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager,
    accountNameChooserFactory: MixinFactory<AccountNameChooserMixin.Presentation>,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val payload: ImportAccountPayload,
    importSourceFactory: ImportSourceFactory,
) : BaseViewModel(),
    WithAccountNameChooserMixin {

    override val accountNameChooser: AccountNameChooserMixin.Presentation = accountNameChooserFactory.create(scope = this)

    val importSource = importSourceFactory.create(
        secretType = payload.type,
        scope = this,
        payload = payload.addAccountPayload,
        accountNameChooserMixin = accountNameChooser
    )

    private val importInProgressFlow = MutableStateFlow(false)

    private val nextButtonEnabledFlow = combine(
        importSource.fieldsValidFlow,
        accountNameChooser.nameValid.asFlow(),
    ) { fieldsValid, nameValid -> fieldsValid and nameValid }

    val nextButtonState = nextButtonEnabledFlow.combine(importInProgressFlow) { enabled, inProgress ->
        when {
            inProgress -> ButtonState.PROGRESS
            enabled -> ButtonState.NORMAL
            else -> ButtonState.DISABLED
        }
    }

    fun homeButtonClicked() {
        router.back()
    }

    fun optionsClicked() {
        advancedEncryptionRequester.openRequest(AdvancedEncryptionPayload.Change(payload.addAccountPayload))
    }

    fun nextClicked() = launch {
        importInProgressFlow.withFlagSet {
            val nameState = accountNameChooser.nameState.value!!
            val addAccountType = mapAddAccountPayloadToAddAccountType(payload.addAccountPayload, nameState)

            importSource.performImport(addAccountType)
                .onSuccess { continueBasedOnCodeStatus() }
                .onFailure(::handleCreateAccountError)
        }
    }

    fun systemCallResultReceived(requestCode: Int, intent: Intent) {
        if (importSource is FileRequester) {
            val currentRequestCode = importSource.chooseJsonFileEvent.value!!.peekContent()

            if (requestCode == currentRequestCode) {
                importSource.fileChosen(intent.data!!)
            }
        }
    }

    private suspend fun continueBasedOnCodeStatus() {
        if (interactor.isCodeSet()) {
            router.openMain()
        } else {
            router.openCreatePincode()
        }
    }

    private fun handleCreateAccountError(throwable: Throwable) {
        var errorMessage = importSource.handleError(throwable)

        if (errorMessage == null) {
            errorMessage = when (throwable) {
                is AccountAlreadyExistsException -> ImportError(
                    titleRes = R.string.account_add_already_exists_message,
                    messageRes = R.string.account_error_try_another_one
                )
                is JunctionDecoder.DecodingError, is BIP32JunctionDecoder.DecodingError -> ImportError(
                    titleRes = R.string.account_invalid_derivation_path_title,
                    messageRes = R.string.account_invalid_derivation_path_message_v2_2_0
                )
                else -> ImportError()
            }
        }

        val title = resourceManager.getString(errorMessage.titleRes)
        val message = resourceManager.getString(errorMessage.messageRes)

        showError(title, message)
    }
}
