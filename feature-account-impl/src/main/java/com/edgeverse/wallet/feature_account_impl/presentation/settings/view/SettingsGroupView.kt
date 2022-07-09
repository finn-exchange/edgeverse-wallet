package com.edgeverse.wallet.feature_account_impl.presentation.settings.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import com.edgeverse.wallet.common.utils.getDrawableCompat
import com.edgeverse.wallet.feature_account_impl.R

class SettingsGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        outlineProvider = ViewOutlineProvider.BACKGROUND
        clipToOutline = true

        orientation = VERTICAL

        //background = context.getRoundedCornerDrawable(fillColorRes = R.color.settings_group_background)

        setBackgroundColor(Color.BLACK)

        dividerDrawable = context.getDrawableCompat(R.drawable.divider_decoration)
        showDividers = SHOW_DIVIDER_MIDDLE
    }
}
