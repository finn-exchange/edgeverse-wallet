package com.edgeverse.wallet.feature_account_api.presenatation.actions

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.resources.ClipboardManager
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.runtime.ext.accountUrlOf
import com.edgeverse.wallet.runtime.ext.eventUrlOf
import com.edgeverse.wallet.runtime.ext.extrinsicUrlOf
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

class ExternalActionsProvider(
    val clipboardManager: ClipboardManager,
    val resourceManager: ResourceManager,
) : ExternalActions.Presentation {

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    override val showExternalActionsEvent = MutableLiveData<Event<ExternalActions.Payload>>()

    override fun viewExternalClicked(explorer: Chain.Explorer, type: ExternalActions.Type) {
        val url = when (type) {
            is ExternalActions.Type.Address -> explorer.accountUrlOf(type.address)
            is ExternalActions.Type.Event -> explorer.eventUrlOf(type.id)
            is ExternalActions.Type.Extrinsic -> explorer.extrinsicUrlOf(type.hash)
            is ExternalActions.Type.None -> null
        }

        url?.let { showBrowser(url) }
    }

    override fun showBrowser(url: String) {
        openBrowserEvent.value = Event(url)
    }

    override fun showExternalActions(type: ExternalActions.Type, chain: Chain) {
        val copyLabelRes = when (type) {
            is ExternalActions.Type.Address -> R.string.common_copy_address
            is ExternalActions.Type.Event -> R.string.common_copy_id
            is ExternalActions.Type.Extrinsic -> R.string.transaction_details_copy_hash
            is ExternalActions.Type.None -> null
        }

        val payload = ExternalActions.Payload(
            type = type,
            chain = chain,
            copyLabelRes = copyLabelRes
        )

        showExternalActionsEvent.value = Event(payload)
    }

    override fun copyAddress(address: String, messageShower: (message: String) -> Unit) {
        clipboardManager.addToClipboard(address)

        val message = resourceManager.getString(R.string.common_copied)

        messageShower.invoke(message)
    }
}
