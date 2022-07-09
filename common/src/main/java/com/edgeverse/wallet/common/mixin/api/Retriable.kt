package com.edgeverse.wallet.common.mixin.api

import androidx.lifecycle.LiveData
import com.edgeverse.wallet.common.utils.Event

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
