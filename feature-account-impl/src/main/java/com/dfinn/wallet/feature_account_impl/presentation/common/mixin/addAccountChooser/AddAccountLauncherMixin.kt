package com.dfinn.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser

import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain

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
