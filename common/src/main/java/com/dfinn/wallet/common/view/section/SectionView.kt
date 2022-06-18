package com.dfinn.wallet.common.view.section

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.dfinn.wallet.common.R
import com.dfinn.wallet.common.utils.WithContextExtensions

abstract class SectionView(
    layoutId: Int,
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions {

    override val providedContext: Context = context

    init {
        View.inflate(context, layoutId, this)

        background = with(context) {
            addRipple(getRoundedCornerDrawable(R.color.white_8))
        }
    }
}
