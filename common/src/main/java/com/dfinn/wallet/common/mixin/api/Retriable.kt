package com.dfinn.wallet.common.mixin.api

import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.utils.Event

typealias Action = () -> Unit

class RetryPayload(
    val title: String,
    val message: String,
    val onRetry: Action,
    val onCancel: Action? = null
)

interface Retriable {

    val retryEvent: LiveData<Event<RetryPayload>>
}
