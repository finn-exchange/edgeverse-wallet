package com.edgeverse.wallet.feature_assets.presentation.transaction.history

import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import coil.clear
import com.edgeverse.wallet.common.list.BaseGroupedDiffCallback
import com.edgeverse.wallet.common.list.GroupedListAdapter
import com.edgeverse.wallet.common.list.GroupedListHolder
import com.edgeverse.wallet.common.utils.formatDaysSinceEpoch
import com.edgeverse.wallet.common.utils.images.setIcon
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.setTextColorRes
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.feature_assets.presentation.model.OperationModel
import com.edgeverse.wallet.feature_assets.presentation.model.OperationStatusAppearance
import com.edgeverse.wallet.feature_assets.presentation.transaction.history.model.DayHeader
import kotlinx.android.synthetic.main.item_day_header.view.itemDayHeader
import kotlinx.android.synthetic.main.item_transaction.view.itemTransactionAmount
import kotlinx.android.synthetic.main.item_transaction.view.itemTransactionHeader
import kotlinx.android.synthetic.main.item_transaction.view.itemTransactionIcon
import kotlinx.android.synthetic.main.item_transaction.view.itemTransactionStatus
import kotlinx.android.synthetic.main.item_transaction.view.itemTransactionSubHeader
import kotlinx.android.synthetic.main.item_transaction.view.itemTransactionTime

class TransactionHistoryAdapter(
    private val handler: Handler,
    private val imageLoader: ImageLoader,
) : GroupedListAdapter<DayHeader, OperationModel>(TransactionHistoryDiffCallback) {

    interface Handler {

        fun transactionClicked(transactionModel: OperationModel)
    }

    override fun createGroupViewHolder(parent: ViewGroup): GroupedListHolder {
        return DayHolder(parent.inflateChild(R.layout.item_day_header))
    }

    override fun createChildViewHolder(parent: ViewGroup): GroupedListHolder {
        return TransactionHolder(parent.inflateChild(R.layout.item_transaction), imageLoader)
    }

    override fun bindGroup(holder: GroupedListHolder, group: DayHeader) {
        (holder as DayHolder).bind(group)
    }

    override fun bindChild(holder: GroupedListHolder, child: OperationModel) {
        (holder as TransactionHolder).bind(child, handler)
    }
}

class TransactionHolder(view: View, private val imageLoader: ImageLoader) : GroupedListHolder(view) {

    fun bind(item: OperationModel, handler: TransactionHistoryAdapter.Handler) {
        with(containerView) {
            with(item) {
                itemTransactionHeader.text = header

                itemTransactionAmount.setTextColorRes(amountColorRes)
                itemTransactionAmount.text = amount

                itemTransactionTime.text = formattedTime

                itemTransactionSubHeader.text = subHeader

                if (statusAppearance != OperationStatusAppearance.COMPLETED) {
                    itemTransactionStatus.makeVisible()
                    itemTransactionStatus.setImageResource(statusAppearance.icon)
                } else {
                    itemTransactionStatus.makeGone()
                }

                setOnClickListener { handler.transactionClicked(this) }
            }

            itemTransactionIcon.setIcon(item.operationIcon, imageLoader)
        }
    }

    override fun unbind() {
        containerView.itemTransactionIcon.clear()
    }
}

class DayHolder(view: View) : GroupedListHolder(view) {
    fun bind(item: DayHeader) {
        with(containerView) {
            itemDayHeader.text = item.daysSinceEpoch.formatDaysSinceEpoch(context)
        }
    }
}

object TransactionHistoryDiffCallback : BaseGroupedDiffCallback<DayHeader, OperationModel>(DayHeader::class.java) {
    override fun areGroupItemsTheSame(oldItem: DayHeader, newItem: DayHeader): Boolean {
        return oldItem.daysSinceEpoch == oldItem.daysSinceEpoch
    }

    override fun areGroupContentsTheSame(oldItem: DayHeader, newItem: DayHeader): Boolean {
        return true
    }

    override fun areChildItemsTheSame(oldItem: OperationModel, newItem: OperationModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areChildContentsTheSame(oldItem: OperationModel, newItem: OperationModel): Boolean {
        return oldItem.statusAppearance == newItem.statusAppearance &&
            oldItem.header == newItem.header &&
            oldItem.subHeader == newItem.subHeader &&
            oldItem.amount == newItem.amount
    }
}
