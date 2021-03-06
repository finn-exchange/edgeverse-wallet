package com.edgeverse.wallet.common.mixin.impl

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseFragmentMixin
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.themed
import com.edgeverse.wallet.common.view.dialog.dialog

class CustomDialogProvider : CustomDialogDisplayer.Presentation {

    override val showCustomDialog = MutableLiveData<Event<CustomDialogDisplayer.Payload>>()

    override fun displayDialog(payload: CustomDialogDisplayer.Payload) {
        showCustomDialog.postValue(Event(payload))
    }
}

fun <V> BaseFragmentMixin<V>.setupCustomDialogDisplayer(
    viewModel: V,
) where V : BaseViewModel, V : CustomDialogDisplayer {
    viewModel.showCustomDialog.observeEvent {
        displayDialogFor(it)
    }
}

fun BaseFragmentMixin<*>.displayDialogFor(payload: CustomDialogDisplayer.Payload) {
    val themedContext = payload.customStyle?.let(providedContext::themed) ?: providedContext

    dialog(themedContext) {
        setTitle(payload.title)
        setMessage(payload.message)

        setPositiveButton(payload.okAction.title) { _, _ ->
            payload.okAction.action()
        }

        payload.cancelAction?.let { negativeAction ->
            setNegativeButton(negativeAction.title) { _, _ ->
                negativeAction.action()
            }
        }
    }
}
