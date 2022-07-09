package com.edgeverse.wallet.feature_assets.presentation.balance.assetActions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.common.utils.dp
import com.edgeverse.wallet.common.utils.updatePadding
import com.edgeverse.wallet.common.view.shape.getBlurDrawable
import kotlinx.android.synthetic.main.view_asset_actions.view.assetActionsBuy
import kotlinx.android.synthetic.main.view_asset_actions.view.assetActionsReceive
import kotlinx.android.synthetic.main.view_asset_actions.view.assetActionsSend

class AssetActionsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    init {
        orientation = HORIZONTAL

        View.inflate(context, R.layout.view_asset_actions, this)

        background = context.getBlurDrawable()

        updatePadding(top = 4.dp(context), bottom = 4.dp(context))
    }

    val send: TextView
        get() = assetActionsSend

    val receive: TextView
        get() = assetActionsReceive

    val buy: TextView
        get() = assetActionsBuy
}
