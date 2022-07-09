package com.edgeverse.wallet.feature_account_api.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.utils.WithContextExtensions
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.utils.useAttributes
import com.edgeverse.wallet.feature_account_api.R
import kotlinx.android.synthetic.main.view_address.view.addressAction
import kotlinx.android.synthetic.main.view_address.view.addressPrimaryIcon
import kotlinx.android.synthetic.main.view_address.view.addressSubtitle
import kotlinx.android.synthetic.main.view_address.view.addressTitle

private const val SHOW_BACKGROUND_DEFAULT = true

class AddressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    init {
        View.inflate(context, R.layout.view_address, this)

        attrs?.let(::applyAttributes)
    }

    fun setAddressModel(addressModel: AddressModel) {
        if (addressModel.name != null) {
            addressTitle.text = addressModel.name
            addressSubtitle.text = addressModel.address

            addressSubtitle.makeVisible()
        } else {
            addressTitle.text = addressModel.address

            addressSubtitle.makeGone()
        }

        addressPrimaryIcon.setImageDrawable(addressModel.image)
    }

    fun setShowBackground(shouldShow: Boolean) {
        background = if (shouldShow) {
            getRoundedCornerDrawable(R.color.white_8, cornerSizeDp = 12).withRipple()
        } else {
            null
        }
    }

    fun setActionClickListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

    fun setActionIcon(icon: Drawable?) {
        addressAction.setImageDrawable(icon)
        addressAction.setVisible(icon != null)
    }

    fun setActionIcon(@DrawableRes icon: Int?) {
        setActionIcon(icon?.let(context::getDrawable))
    }

    private fun applyAttributes(attrs: AttributeSet) = context.useAttributes(attrs, R.styleable.AddressView) { typedArray ->
        val actionIcon = typedArray.getDrawable(R.styleable.AddressView_actionIcon)
        setActionIcon(actionIcon)

        val shouldShowBackground = typedArray.getBoolean(R.styleable.AddressView_showBackground, SHOW_BACKGROUND_DEFAULT)
        setShowBackground(shouldShowBackground)
    }
}
