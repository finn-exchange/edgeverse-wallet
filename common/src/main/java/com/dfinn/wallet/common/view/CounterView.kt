package com.dfinn.wallet.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import com.dfinn.wallet.common.R
import com.dfinn.wallet.common.view.shape.getRoundedCornerDrawable

class CounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(ContextThemeWrapper(context, R.style.Widget_Nova_Counter), attrs, defStyleAttr) {

    init {
        background = context.getRoundedCornerDrawable(R.color.white_16, cornerSizeInDp = 6)
    }
}
