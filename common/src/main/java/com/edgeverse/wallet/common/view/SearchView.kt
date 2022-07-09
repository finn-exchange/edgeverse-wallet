package com.edgeverse.wallet.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.utils.*
import kotlinx.android.synthetic.main.view_search.view.*

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), WithContextExtensions {

    override val providedContext: Context
        get() = context

    val content: EditText
        get() = searchContent

    fun setHint(hint: String?) {
        content.hint = hint
    }

    fun setIcon(@DrawableRes icon: Int?) {
        searchContent.setDrawableStart(icon, widthInDp = 16, paddingInDp = 6, tint = R.color.white_32)
    }

    init {
        View.inflate(context, R.layout.view_search, this)

        background = getRoundedCornerDrawable(fillColorRes = R.color.white_8)

        orientation = HORIZONTAL

        content.onTextChanged {
            searchClear.setVisible(it.isNotEmpty())
        }
        searchClear.setOnClickListener {
            content.text.clear()
        }

        attrs?.let(::applyAttrs)
    }

    private fun applyAttrs(attributeSet: AttributeSet) = context.useAttributes(attributeSet, R.styleable.SearchView) {
        val hint = it.getString(R.styleable.SearchView_android_hint)
        setHint(hint)

        val icon = it.getResourceIdOrNull(R.styleable.SearchView_icon)
        setIcon(icon)
    }
}
