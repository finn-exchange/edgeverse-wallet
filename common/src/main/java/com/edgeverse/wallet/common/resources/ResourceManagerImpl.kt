package com.edgeverse.wallet.common.resources

import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import androidx.core.content.ContextCompat
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.common.utils.daysFromMillis
import com.edgeverse.wallet.common.utils.formatDateTime
import com.edgeverse.wallet.common.utils.getDrawableCompat
import com.edgeverse.wallet.common.utils.readText
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@ApplicationScope
class ResourceManagerImpl(
    private val contextManager: ContextManager
) : ResourceManager {

    override fun loadRawString(res: Int): String {
        return contextManager.getContext().resources
            .openRawResource(res)
            .readText()
    }

    override fun getString(res: Int): String {
        return contextManager.getContext().getString(res)
    }

    override fun getString(res: Int, vararg arguments: Any): String {
        return contextManager.getContext().getString(res, *arguments)
    }

    override fun getColor(res: Int): Int {
        return ContextCompat.getColor(contextManager.getContext(), res)
    }

    override fun getQuantityString(id: Int, quantity: Int): String {
        return contextManager.getContext().resources.getQuantityString(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg arguments: Any): String {
        return contextManager.getContext().resources.getQuantityString(id, quantity, *arguments)
    }

    override fun measureInPx(dp: Int): Int {
        val px = contextManager.getContext().resources.displayMetrics.density * dp

        return px.toInt()
    }

    override fun formatDate(timestamp: Long): String {
        return timestamp.formatDateTime(contextManager.getContext()).toString()
    }

    override fun formatTime(timestamp: Long): String {
        return DateUtils.formatDateTime(contextManager.getContext(), timestamp, DateUtils.FORMAT_SHOW_TIME)
    }

    @OptIn(ExperimentalTime::class)
    override fun formatDuration(elapsedTime: Long): String {
        val inDays = elapsedTime.daysFromMillis().toInt()

        return when {
            inDays > 0 -> getQuantityString(R.plurals.staking_main_lockup_period_value, inDays, inDays)
            else -> {
                val inSeconds = elapsedTime.milliseconds.inSeconds.toLong()

                DateUtils.formatElapsedTime(inSeconds)
            }
        }
    }

    override fun getDrawable(id: Int): Drawable {
        return contextManager.getContext().getDrawableCompat(id)
    }
}
