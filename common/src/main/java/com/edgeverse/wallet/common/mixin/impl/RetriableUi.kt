package com.edgeverse.wallet.common.mixin.impl

import android.content.Context
import com.edgeverse.wallet.common.base.BaseFragmentMixin
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Retriable
import com.edgeverse.wallet.common.view.dialog.retryDialog

fun BaseFragmentMixin<*>.observeRetries(
    retriable: Retriable,
    context: Context = fragment.requireContext(),
) {
    retriable.retryEvent.observeEvent {
        retryDialog(
            context = context,
            onRetry = it.onRetry,
            onCancel = it.onCancel
        ) {
            setTitle(it.title)
            setMessage(it.message)
        }
    }
}

fun <T> BaseFragmentMixin<T>.observeRetries(
    viewModel: T,
    context: Context = fragment.requireContext(),
) where T : BaseViewModel, T : Retriable {
    observeRetries(retriable = viewModel, context)
}
