package com.edgeverse.wallet.common.view.input

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.view.shape.getInputBackground

class TextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.defaultTextInputStyle,
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        background = context.getInputBackground()
    }
}
