package com.dfinn.wallet.feature_wallet_api.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.dfinn.wallet.common.utils.dp
import com.dfinn.wallet.common.utils.setTextColorRes
import com.dfinn.wallet.common.utils.updatePadding
import com.dfinn.wallet.common.utils.useAttributes
import com.dfinn.wallet.common.view.TableCellView
import com.dfinn.wallet.common.view.shape.getCutCornersStateDrawable
import com.dfinn.wallet.feature_wallet_api.R
import com.dfinn.wallet.feature_wallet_api.presentation.model.AmountModel
import kotlinx.android.synthetic.main.view_balances.view.*

abstract class BalancesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.view_balances, this)
        orientation = VERTICAL

        val commonPadding = 16.dp(context)

        updatePadding(
            top = commonPadding,
            start = commonPadding,
            end = commonPadding,
            bottom = 8.dp(context)
        )

        attrs?.let {
            applyAttributes(it)
        }

        background = context.getCutCornersStateDrawable()
    }

    private fun applyAttributes(attributes: AttributeSet) = context.useAttributes(attributes, R.styleable.BalancesView) {
        val title = it.getString(R.styleable.BalancesView_title)
        viewBalancesTitle.text = title
    }

    protected fun item(@StringRes titleRes: Int): TableCellView {
        val item = TableCellView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            setDividerColor(R.color.white_24)

            valueSecondary.setTextColorRes(R.color.white_64)
            title.setTextColorRes(R.color.white_64)

            setTitle(titleRes)
        }

        addView(item)

        return item
    }
}

fun TableCellView.showAmount(amountModel: AmountModel) {
    showValue(amountModel.token, amountModel.fiat)
}
