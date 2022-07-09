package com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser

import androidx.lifecycle.LiveData
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

interface AddAccountLauncherMixin {

    class AddAccountTypePayload(
        val title: String,
        val onCreate: () -> Unit,
        val onImport: () -> Unit
    )

    val showAddAccountTypeChooser: LiveData<Event<AddAccountTypePayload>>

    val showImportTypeChooser: LiveData<Event<ImportTypeChooserMixin.Payload>>

    interface Presentation : AddAccountLauncherMixin {

        enum class Mode {
            ADD, CHANGE
        }

        fun initiateLaunch(
            chain: Chain,
            metaAccountId: Long,
            mode: Mode
        )
    }
}
