package com.edgeverse.wallet.feature_dapp_impl.presentation.main.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import com.edgeverse.wallet.common.utils.WithContextExtensions
import com.edgeverse.wallet.common.utils.setDrawableStart
import com.edgeverse.wallet.common.utils.setTextColorRes
import com.edgeverse.wallet.common.view.shape.getCutCornersStateDrawable
import com.edgeverse.wallet.feature_dapp_impl.R

class TapToSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(ContextThemeWrapper(context, R.style.TextAppearance_NovaFoundation_Regular_SubHeadline), attrs, defStyleAttr),
    WithContextExtensions {

    override val providedContext: Context
        get() = context

    init {
        setPaddingRelative(12.dp, 16.dp, 12.dp, 16.dp)

        setDrawableStart(
            drawableRes = R.drawable.ic_search,
            widthInDp = 20,
            heightInDp = 20,
            paddingInDp = 8,
            tint = R.color.white_32
        )

        text = context.getString(R.string.dapp_search_hint)
        setTextColorRes(R.color.white_48)

        background = context.getCutCornersStateDrawable()
    }
}
