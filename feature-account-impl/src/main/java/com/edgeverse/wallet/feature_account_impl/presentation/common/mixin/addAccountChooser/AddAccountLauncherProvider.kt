package com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.event
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.SecretType
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherMixin.Presentation
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherMixin.Presentation.Mode
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

class AddAccountLauncherProvider(
    private val importTypeChooserMixin: ImportTypeChooserMixin.Presentation,
    private val resourceManager: ResourceManager,
    private val router: AccountRouter
) : Presentation {

    private fun addAccountSelected(chainAccountPayload: AddAccountPayload.ChainAccount) {
        router.openMnemonicScreen(accountName = null, chainAccountPayload)
    }

    private fun importAccountSelected(chainAccountPayload: AddAccountPayload.ChainAccount) {
        val payload = ImportTypeChooserMixin.Payload(
            onChosen = { importTypeSelected(chainAccountPayload, it) }
        )

        importTypeChooserMixin.showChooser(payload)
    }

    private fun importTypeSelected(chainAccountPayload: AddAccountPayload.ChainAccount, secretType: SecretType) {
        router.openImportAccountScreen(ImportAccountPayload(secretType, chainAccountPayload))
    }

    override fun initiateLaunch(chain: Chain, metaAccountId: Long, mode: Mode) {
        val chainAccountPayload = AddAccountPayload.ChainAccount(chain.id, metaAccountId)

        val titleTemplate = when (mode) {
            Mode.CHANGE -> R.string.accounts_change_chain_account
            Mode.ADD -> R.string.accounts_add_chain_account
        }
        val title = resourceManager.getString(titleTemplate, chain.name)

        showAddAccountTypeChooser.value = AddAccountLauncherMixin.AddAccountTypePayload(
            title = title,
            onCreate = { addAccountSelected(chainAccountPayload) },
            onImport = { importAccountSelected(chainAccountPayload) }
        ).event()
    }

    override val showAddAccountTypeChooser = MutableLiveData<Event<AddAccountLauncherMixin.AddAccountTypePayload>>()

    override val showImportTypeChooser: LiveData<Event<ImportTypeChooserMixin.Payload>> = importTypeChooserMixin.showChooserEvent
}
