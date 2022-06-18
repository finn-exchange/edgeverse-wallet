package com.dfinn.wallet.feature_account_api.presenatation.actions

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.mixin.api.Browserable
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.runtime.ext.addressOf
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ExplorerTemplateExtractor
import jp.co.soramitsu.fearless_utils.runtime.AccountId

interface ExternalActions : Browserable {

    class Payload(
        val type: Type,
        val chain: Chain,
        @StringRes val copyLabelRes: Int?,
    )

    sealed class Type(
        val primaryValue: String,
        val explorerTemplateExtractor: ExplorerTemplateExtractor?,
    ) {

        class None(primaryValue: String) : Type(primaryValue, explorerTemplateExtractor = null)

        class Address(val address: String) : Type(address, explorerTemplateExtractor = Chain.Explorer::account)

        class Extrinsic(val hash: String) : Type(hash, explorerTemplateExtractor = Chain.Explorer::extrinsic)

        class Event(val id: String) : Type(id, explorerTemplateExtractor = Chain.Explorer::event)
    }

    val showExternalActionsEvent: LiveData<Event<Payload>>

    fun viewExternalClicked(explorer: Chain.Explorer, type: Type)

    fun copyAddress(address: String, messageShower: (message: String) -> Unit)

    interface Presentation : ExternalActions, Browserable.Presentation {

        fun showExternalActions(type: Type, chain: Chain)
    }
}

fun ExternalActions.Presentation.showAddressActions(accountId: AccountId, chain: Chain) = showExternalActions(
    type = ExternalActions.Type.Address(chain.addressOf(accountId)),
    chain = chain
)
