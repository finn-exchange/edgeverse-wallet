package com.dfinn.wallet.feature_account_impl.presentation.mnemonic.confirm.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.dfinn.wallet.common.utils.useAttributes
import com.dfinn.wallet.common.view.shape.getRoundedCornerDrawable
import com.dfinn.wallet.feature_account_impl.R

private const val SHOW_BACKGROUND_DEFAULT = true

class MnemonicContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP).apply {
            justifyContent = JustifyContent.CENTER
        }

        attrs?.let(::applyAttrs)
    }

    private fun setShowBackground(show: Boolean) {
        val background = if (show) {
            context.getRoundedCornerDrawable(fillColorRes = R.color.white_8)
        } else {
            null
        }

        setBackground(background)
    }

    private fun applyAttrs(attributeSet: AttributeSet) = context.useAttributes(attributeSet, R.styleable.MnemonicContainerView) {
        val showBackground = it.getBoolean(R.styleable.MnemonicContainerView_showBackground, SHOW_BACKGROUND_DEFAULT)
        setShowBackground(showBackground)
    }
}
