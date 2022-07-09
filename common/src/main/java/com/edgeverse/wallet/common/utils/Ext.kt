package com.edgeverse.wallet.common.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.net.URLEncoder

fun Activity.showToast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun Fragment.hideKeyboard() {
    requireActivity().currentFocus?.hideSoftKeyboard()
}

fun Fragment.showBrowser(link: String) = requireContext().showBrowser(link)

fun Context.showBrowser(link: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(link) }

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.common_cannot_open_link, Toast.LENGTH_SHORT)
            .show()
    }
}

fun Context.sendEmailIntent(
    targetEmail: String,
    title: String = getString(R.string.common_email_chooser_title)
) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        putExtra(Intent.EXTRA_EMAIL, targetEmail)
        type = "message/rfc822"
        data = Uri.parse("mailto:$targetEmail")
    }
    startActivity(Intent.createChooser(emailIntent, title))
}

fun @receiver:ColorInt Int.toHexColor(): String {
    val withoutAlpha = 0xFFFFFF and this

    return "#%06X".format(withoutAlpha)
}

fun String.urlEncoded() = URLEncoder.encode(this, Charsets.UTF_8.displayName())

fun CoroutineScope.childScope(supervised: Boolean = true): CoroutineScope {
    val parentJob = coroutineContext[Job]

    val job = if (supervised) SupervisorJob(parent = parentJob) else Job(parent = parentJob)

    return CoroutineScope(coroutineContext + job)
}

fun Int.asBoolean() = this != 0