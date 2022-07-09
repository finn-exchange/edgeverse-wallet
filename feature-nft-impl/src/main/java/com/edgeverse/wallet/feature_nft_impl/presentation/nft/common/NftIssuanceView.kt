package com.edgeverse.wallet.feature_nft_impl.presentation.nft.common

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.edgeverse.wallet.common.utils.WithContextExtensions
import com.edgeverse.wallet.common.utils.setTextColorRes
import com.edgeverse.wallet.common.utils.updatePadding
import com.edgeverse.wallet.common.view.shape.getRoundedCornerDrawable
import com.edgeverse.wallet.nova.feature_nft_impl.R

class NftIssuanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), WithContextExtensions {

    override val providedContext: Context = context

    init {
        setTextAppearance(R.style.TextAppearance_NovaFoundation_SemiBold_Caps2)
        setTextColorRes(R.color.white_64)
        updatePadding(top = 1.5f.dp, bottom = 1.5f.dp, start = 6.dp, end = 8.dp)
        background = context.getRoundedCornerDrawable(R.color.white_16, cornerSizeInDp = 4)
    }
}
