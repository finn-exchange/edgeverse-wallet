package com.edgeverse.wallet.common.view.dialog

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.utils.themed

typealias DialogClickHandler = () -> Unit

typealias DialogDecorator = AlertDialog.Builder.() -> Unit

inline fun dialog(
    context: Context,
    decorator: DialogDecorator
) {
    val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.WhiteOverlay))
        .setCancelable(false)

    builder.decorator()

    builder.show()
}

fun infoDialog(
    context: Context,
    decorator: DialogDecorator
) {
    dialog(context) {
        setPositiveButton(R.string.common_ok, null)

        decorator()
    }
}

fun warningDialog(
    context: Context,
    onConfirm: DialogClickHandler,
    @StringRes confirmTextRes: Int = R.string.common_continue,
    onCancel: DialogClickHandler? = null,
    decorator: DialogDecorator? = null
) {
    dialog(context.themed(R.style.AccentAlertDialogTheme_Reversed)) {
        setPositiveButton(confirmTextRes) { _, _ -> onConfirm() }
        setNegativeButton(R.string.common_cancel) { _, _ -> onCancel?.invoke() }

        decorator?.invoke(this)
    }
}

fun errorDialog(
    context: Context,
    onConfirm: DialogClickHandler? = null,
    decorator: DialogDecorator? = null
) {
    dialog(context) {
        setTitle(R.string.common_error_general_title)
        setPositiveButton(R.string.common_ok) { _, _ -> onConfirm?.invoke() }

        decorator?.invoke(this)
    }
}

fun retryDialog(
    context: Context,
    onRetry: DialogClickHandler? = null,
    onCancel: DialogClickHandler? = null,
    decorator: DialogDecorator? = null
) {
    dialog(context) {
        setTitle(R.string.common_error_general_title)
        setPositiveButton(R.string.common_retry) { _, _ -> onRetry?.invoke() }
        setNegativeButton(R.string.common_ok) { _, _ -> onCancel?.invoke() }

        decorator?.invoke(this)
    }
}
