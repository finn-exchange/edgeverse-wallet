package com.edgeverse.wallet.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.view.shape.addRipple
import com.edgeverse.wallet.common.view.shape.getIdleDrawable
import kotlinx.android.synthetic.main.view_account_info.view.*

class AccountInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_account_info, this)

        background = with(context) { addRipple(getIdleDrawable()) }

        isFocusable = true
        isClickable = true

        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccountInfoView)

            val actionIcon = typedArray.getDrawable(R.styleable.AccountInfoView_accountActionIcon)
            actionIcon?.let(::setActionIcon)

            val textVisible = typedArray.getBoolean(R.styleable.AccountInfoView_textVisible, true)
            accountAddressText.visibility = if (textVisible) View.VISIBLE else View.GONE

            typedArray.recycle()
        }
    }

    fun setActionIcon(icon: Drawable) {
        accountAction.setImageDrawable(icon)
    }

    fun setActionListener(clickListener: (View) -> Unit) {
        accountAction.setOnClickListener(clickListener)
    }

    fun setWholeClickListener(listener: (View) -> Unit) {
        setOnClickListener(listener)

        setActionListener(listener)
    }

    fun setTitle(accountName: String) {
        accountTitle.text = accountName
    }

    fun setText(address: String) {
        accountAddressText.text = address
    }

    fun setAccountIcon(icon: Drawable) {
        accountIcon.setImageDrawable(icon)
    }

    fun hideBody() {
        accountAddressText.makeGone()
    }

    fun showBody() {
        accountAddressText.makeVisible()
    }
}
