package com.dfinn.wallet.feature_wallet_api.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.dfinn.wallet.common.utils.setBackgroundColorRes
import com.dfinn.wallet.common.view.PrimaryButton
import com.dfinn.wallet.feature_wallet_api.R
import kotlinx.android.synthetic.main.view_confirm_transaction.view.confirmTransactionAction
import kotlinx.android.synthetic.main.view_confirm_transaction.view.confirmTransactionFee

class ConfirmTransactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    val fee: FeeView
        get() = confirmTransactionFee

    val submit: PrimaryButton
        get() = confirmTransactionAction

    init {
        View.inflate(context, R.layout.view_confirm_transaction, this)

        orientation = VERTICAL

        setBackgroundColorRes(R.color.black4)
    }
}
