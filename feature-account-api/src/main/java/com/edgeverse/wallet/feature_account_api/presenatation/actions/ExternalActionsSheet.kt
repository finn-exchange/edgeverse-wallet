package com.edgeverse.wallet.feature_account_api.presenatation.actions

import android.content.Context
import android.os.Bundle
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.view.bottomSheet.list.fixed.FixedListBottomSheet
import com.edgeverse.wallet.common.view.bottomSheet.list.fixed.item
import com.edgeverse.wallet.runtime.ext.availableExplorersFor
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

typealias ExternalViewCallback = (Chain.Explorer, ExternalActions.Type) -> Unit
typealias CopyCallback = (String) -> Unit

open class ExternalActionsSheet(
    context: Context,
    protected val payload: ExternalActions.Payload,
    val onCopy: CopyCallback,
    val onViewExternal: ExternalViewCallback,
) : FixedListBottomSheet(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(payload.type.primaryValue)

        payload.copyLabelRes?.let {
            item(R.drawable.ic_copy, it) {
                onCopy(payload.type.primaryValue)
            }
        }

        payload.type.explorerTemplateExtractor?.let {
            payload.chain
                .availableExplorersFor(it)
                .forEach { explorer ->
                    val title = context.getString(R.string.transaction_details_view_explorer, explorer.name)

                    item(R.drawable.ic_globe_outline, title) {
                        onViewExternal(explorer, payload.type)
                    }
                }
        }
    }
}
