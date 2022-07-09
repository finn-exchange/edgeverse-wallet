package com.edgeverse.wallet.common.mixin.api

import androidx.annotation.StyleRes
import androidx.lifecycle.LiveData
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event

interface CustomDialogDisplayer {

    val showCustomDialog: LiveData<Event<Payload>>

    class Payload(
        val title: String,
        val message: String,
        val okAction: DialogAction,
        val cancelAction: DialogAction? = null,
        @StyleRes val customStyle: Int? = null,
    ) {

        class DialogAction(
            val title: String,
            val action: () -> Unit,
        ) {

            companion object {

                fun noOp(title: String) = DialogAction(title = title, action = {})
            }
        }
    }

    interface Presentation : CustomDialogDisplayer {

        fun displayDialog(payload: Payload)
    }
}

fun CustomDialogDisplayer.Presentation.displayError(
    resourceManager: ResourceManager,
    error: Throwable,
) {
    error.message?.let {
        displayDialog(
            CustomDialogDisplayer.Payload(
                title = resourceManager.getString(R.string.common_error_general_title),
                message = it,
                okAction = CustomDialogDisplayer.Payload.DialogAction.noOp(resourceManager.getString(R.string.common_ok)),
                cancelAction = null
            ),
        )
    }
}
