package com.edgeverse.wallet.feature_assets.presentation.balance.detail

import android.content.Context
import android.util.AttributeSet
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.common.utils.setDrawableEnd
import com.edgeverse.wallet.feature_wallet_api.presentation.view.BalancesView

class AssetDetailBalancesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : BalancesView(context, attrs, defStyle) {

    val total = item(R.string.wallet_send_total_title)

    val transferable = item(R.string.wallet_balance_transferable)

    val locked = item(R.string.wallet_balance_locked).apply {
        setDividerVisible(false)

        title.setDrawableEnd(R.drawable.ic_info_16, paddingInDp = 4)
    }
}
