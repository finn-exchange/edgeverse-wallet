package io.novafoundation.nova.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.presentation.DescriptiveButtonState
import io.novafoundation.nova.common.utils.doOnGlobalLayout
import io.novafoundation.nova.common.utils.dp
import io.novafoundation.nova.common.utils.setVisible

enum class ButtonState {
    NORMAL,
    DISABLED,
    PROGRESS,
    GONE
}

class PrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AppCompatTextView(ContextThemeWrapper(context, R.style.Widget_Nova_Button), attrs, defStyle) {

    enum class Appearance {

        PRIMARY {
            override fun buttonBackgroundDrawable(context: Context): Drawable? {
                return ContextCompat.getDrawable(context, R.drawable.bg_button_primary_selector)
            }
        };

        abstract fun buttonBackgroundDrawable(context: Context): Drawable?

    }

    enum class Size(val heightDp: Int, val cornerSizeDp: Int) {
        LARGE(52, 12), SMALL(44, 10);
    }

    private var cachedText: String? = null

    private var preparedForProgress = false

    init {
        setAppearance(Appearance.PRIMARY)
    }

    fun prepareForProgress(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.bindProgressButton(this)

        preparedForProgress = true
    }

    fun setState(state: ButtonState) {
        isEnabled = state == ButtonState.NORMAL
        setVisible(state != ButtonState.GONE)

        if (state == ButtonState.PROGRESS) {
            checkPreparedForProgress()

            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun setSize(size: Size) {
        doOnGlobalLayout {
            layoutParams.apply {
                height = size.heightDp.dp(context)
            }
        }
    }

    private fun setAppearance(appearance: Appearance) {
        background = appearance.buttonBackgroundDrawable(context)
    }

    private fun checkPreparedForProgress() {
        if (!preparedForProgress) {
            throw IllegalArgumentException("You must call prepareForProgress() first!")
        }
    }

    private fun hideProgress() {
        if (isProgressActive()) {
            hideProgress(cachedText)
        }
    }

    private fun showProgress() {
        if (isProgressActive()) return

        cachedText = text.toString()

        showProgress {
            progressColorRes = R.color.gray2
        }
    }
}

fun PrimaryButton.setProgress(show: Boolean) {
    setState(if (show) ButtonState.PROGRESS else ButtonState.NORMAL)
}

fun PrimaryButton.setState(descriptiveButtonState: DescriptiveButtonState) {
    when (descriptiveButtonState) {
        is DescriptiveButtonState.Disabled -> {
            setState(ButtonState.DISABLED)
            text = descriptiveButtonState.reason
        }
        is DescriptiveButtonState.Enabled -> {
            setState(ButtonState.NORMAL)
            text = descriptiveButtonState.action
        }
        DescriptiveButtonState.Loading -> {
            setState(ButtonState.PROGRESS)
        }
        DescriptiveButtonState.Gone -> {
            setState(ButtonState.GONE)
        }
    }
}
