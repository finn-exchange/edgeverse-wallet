package com.edgeverse.wallet.common.mixin.actionAwaitable

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class ActionAwaitableProvider<P, R> : ActionAwaitableMixin.Presentation<P, R> {

    companion object : ActionAwaitableMixin.Factory {

        override fun <P, R> create(): ActionAwaitableMixin.Presentation<P, R> {
            return ActionAwaitableProvider()
        }
    }

    override val awaitableActionLiveData = MutableLiveData<Event<ActionAwaitableMixin.Action<P, R>>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun awaitAction(payload: P): R = suspendCancellableCoroutine { continuation ->
        val action = ActionAwaitableMixin.Action<P, R>(
            payload = payload,
            onSuccess = { continuation.resume(it) },
            onCancel = { continuation.cancel() }
        )

        awaitableActionLiveData.value = action.event()
    }
}
