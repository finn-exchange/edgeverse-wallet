package com.edgeverse.wallet.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.utils.WithContextExtensions
import com.edgeverse.wallet.common.utils.setImageTintRes
import com.edgeverse.wallet.common.utils.updatePadding

class ManageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    init {
        updatePadding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)

        setImageResource(R.drawable.ic_options)
        setImageTintRes(R.color.white_64)

        background = addRipple(getRoundedCornerDrawable(R.color.black_48))
    }
}
