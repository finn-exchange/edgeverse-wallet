package io.novafoundation.nova.feature_wallet_api.presentation.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import coil.ImageLoader
import coil.load
import io.novafoundation.nova.common.utils.getEnum
import io.novafoundation.nova.common.utils.useAttributes
import io.novafoundation.nova.common.view.shape.addRipple
import io.novafoundation.nova.common.view.shape.getBlurDrawable
import io.novafoundation.nova.common.view.shape.getCutCornersStateDrawable
import io.novafoundation.nova.common.view.shape.getIdleDrawable
import io.novafoundation.nova.feature_wallet_api.R
import io.novafoundation.nova.feature_wallet_api.presentation.model.AssetModel
import kotlinx.android.synthetic.main.view_asset_selector.view.assetSelectorAction
import kotlinx.android.synthetic.main.view_asset_selector.view.assetSelectorBalance
import kotlinx.android.synthetic.main.view_asset_selector.view.assetSelectorIcon
import kotlinx.android.synthetic.main.view_asset_selector.view.assetSelectorTokenName

class AssetSelectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.view_asset_selector, this)

        background = context.getCutCornersStateDrawable()
    }

    fun setActionIcon(drawable: Drawable) {
        assetSelectorAction.setImageDrawable(drawable)
    }

    fun onClick(action: (View) -> Unit) {
        setOnClickListener(action)
    }

    fun setState(
        imageLoader: ImageLoader,
        assetModel: AssetModel
    ) {
        with(assetModel) {
            assetSelectorBalance.text = assetBalance
            assetSelectorTokenName.text = tokenName
            assetSelectorIcon.load(imageUrl, imageLoader)
        }
    }
}
