package com.dfinn.wallet.feature_account_api.presenatation.mixin.importType

import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.feature_account_api.presenatation.account.add.SecretType

interface ImportTypeChooserMixin {

    class Payload(
        val allowedTypes: Set<SecretType> = SecretType.values().toSet(),
        val onChosen: (SecretType) -> Unit
    )

    val showChooserEvent: LiveData<Event<Payload>>

    interface Presentation : ImportTypeChooserMixin {

        fun showChooser(payload: Payload)
    }
}
