package com.edgeverse.wallet.feature_account_impl.presentation.node.list.accounts

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.ClickHandler
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListSheetAdapter
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.HolderCreator
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.presentation.node.list.accounts.model.AccountByNetworkModel
import kotlinx.android.synthetic.main.item_account_by_network.view.accountIcon
import kotlinx.android.synthetic.main.item_account_by_network.view.accountTitle

class AccountChooserBottomSheetDialog(
    context: Context,
    payload: Payload<AccountByNetworkModel>,
    onClicked: ClickHandler<AccountByNetworkModel>
) : DynamicListBottomSheet<AccountByNetworkModel>(context, payload, AccountDiffCallback, onClicked) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.profile_accounts_title)
    }

    override fun holderCreator(): HolderCreator<AccountByNetworkModel> = {
        AccountHolder(it.inflateChild(R.layout.item_account_by_network))
    }
}

class AccountHolder(
    itemView: View
) : DynamicListSheetAdapter.Holder<AccountByNetworkModel>(itemView) {

    override fun bind(item: AccountByNetworkModel, isSelected: Boolean, handler: DynamicListSheetAdapter.Handler<AccountByNetworkModel>) {
        super.bind(item, isSelected, handler)

        with(itemView) {
            accountTitle.text = item.name.orEmpty()
            accountIcon.setImageDrawable(item.addressModel.image)
        }
    }
}

private object AccountDiffCallback : DiffUtil.ItemCallback<AccountByNetworkModel>() {
    override fun areItemsTheSame(oldItem: AccountByNetworkModel, newItem: AccountByNetworkModel): Boolean {
        return oldItem.accountAddress == newItem.accountAddress
    }

    override fun areContentsTheSame(oldItem: AccountByNetworkModel, newItem: AccountByNetworkModel): Boolean {
        return oldItem == newItem
    }
}
