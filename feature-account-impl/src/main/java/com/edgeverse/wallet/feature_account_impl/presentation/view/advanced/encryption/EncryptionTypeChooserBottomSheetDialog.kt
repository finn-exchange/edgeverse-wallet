package com.edgeverse.wallet.feature_account_impl.presentation.view.advanced.encryption

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.common.utils.makeInvisible
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.ClickHandler
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListSheetAdapter
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.HolderCreator
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.presentation.view.advanced.encryption.model.CryptoTypeModel
import kotlinx.android.synthetic.main.item_encryption_type.view.encryptionTv
import kotlinx.android.synthetic.main.item_encryption_type.view.rightIcon

class EncryptionTypeChooserBottomSheetDialog(
    context: Context,
    payload: Payload<CryptoTypeModel>,
    onClicked: ClickHandler<CryptoTypeModel>
) : DynamicListBottomSheet<CryptoTypeModel>(context, payload, CryptoModelCallback, onClicked) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.common_crypto_type)
    }

    override fun holderCreator(): HolderCreator<CryptoTypeModel> = {
        EncryptionTypeViewHolder(it.inflateChild(R.layout.item_encryption_type))
    }
}

class EncryptionTypeViewHolder(
    itemView: View
) : DynamicListSheetAdapter.Holder<CryptoTypeModel>(itemView) {

    override fun bind(item: CryptoTypeModel, isSelected: Boolean, handler: DynamicListSheetAdapter.Handler<CryptoTypeModel>) {
        super.bind(item, isSelected, handler)

        with(itemView) {
            if (isSelected) {
                rightIcon.makeVisible()
            } else {
                rightIcon.makeInvisible()
            }

            encryptionTv.text = item.name
        }
    }
}

private object CryptoModelCallback : DiffUtil.ItemCallback<CryptoTypeModel>() {
    override fun areItemsTheSame(oldItem: CryptoTypeModel, newItem: CryptoTypeModel): Boolean {
        return oldItem.cryptoType == newItem.cryptoType
    }

    override fun areContentsTheSame(oldItem: CryptoTypeModel, newItem: CryptoTypeModel): Boolean {
        return oldItem == newItem
    }
}
