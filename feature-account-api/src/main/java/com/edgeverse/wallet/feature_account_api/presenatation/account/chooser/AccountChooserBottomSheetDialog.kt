package com.edgeverse.wallet.feature_account_api.presenatation.account.chooser

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.ClickHandler
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListSheetAdapter
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.HolderCreator
import com.edgeverse.wallet.feature_account_api.R
import kotlinx.android.synthetic.main.item_account_chooser.view.itemAccountChooserAddress
import kotlinx.android.synthetic.main.item_account_chooser.view.itemAccountChooserCheck

class AccountChooserBottomSheetDialog(
    context: Context,
    payload: Payload<AddressModel>,
    onSuccess: ClickHandler<AddressModel>,
    onCancel: (() -> Unit)? = null,
    @StringRes val title: Int
) : DynamicListBottomSheet<AddressModel>(
    context = context,
    payload = payload,
    diffCallback = AddressModelDiffCallback,
    onClicked = onSuccess,
    onCancel = onCancel
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(title)
        setDividerVisible(false)
    }

    override fun holderCreator(): HolderCreator<AddressModel> = { parent ->
        AddressModelHolder(parent.inflateChild(R.layout.item_account_chooser))
    }
}

private class AddressModelHolder(parent: View) : DynamicListSheetAdapter.Holder<AddressModel>(parent) {

    override fun bind(
        item: AddressModel,
        isSelected: Boolean,
        handler: DynamicListSheetAdapter.Handler<AddressModel>
    ) {
        super.bind(item, isSelected, handler)

        with(itemView) {
            itemAccountChooserAddress.setAddressModel(item)
            itemAccountChooserCheck.isChecked = isSelected
        }
    }
}

private object AddressModelDiffCallback : DiffUtil.ItemCallback<AddressModel>() {
    override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return true
    }
}
