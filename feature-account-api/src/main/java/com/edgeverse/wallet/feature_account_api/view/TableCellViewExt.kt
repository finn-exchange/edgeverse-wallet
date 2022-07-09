package com.edgeverse.wallet.feature_account_api.view

import androidx.core.view.setPadding
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.utils.dp
import com.edgeverse.wallet.common.view.TableCellView
import com.edgeverse.wallet.feature_account_api.presenatation.chain.ChainUi
import com.edgeverse.wallet.feature_account_api.presenatation.chain.toDrawable

fun TableCellView.showAddress(addressModel: AddressModel) {
    setImage(addressModel.image)

    showValue(addressModel.nameOrAddress)
}

fun TableCellView.showChain(chainUi: ChainUi) {
    image.background = chainUi.gradient.toDrawable(context, cornerRadiusDp = 6)
    image.setPadding(1.5f.dp(context))
    loadImage(chainUi.icon)

    showValue(chainUi.name)
}
