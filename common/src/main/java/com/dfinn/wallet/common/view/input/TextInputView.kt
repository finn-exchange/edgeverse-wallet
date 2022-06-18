package com.dfinn.wallet.common.view.input

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dfinn.wallet.common.R
import com.dfinn.wallet.common.view.shape.getInputBackground

class TextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.defaultTextInputStyle,
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        background = context.getInputBackground()
    }
}
