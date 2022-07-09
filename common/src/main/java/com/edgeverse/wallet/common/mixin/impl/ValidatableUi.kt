package com.edgeverse.wallet.common.mixin.impl

import android.content.Context
import com.edgeverse.wallet.common.base.BaseFragmentMixin
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.mixin.api.ValidationFailureUi
import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.view.dialog.errorDialog
import com.edgeverse.wallet.common.view.dialog.warningDialog

fun BaseFragmentMixin<*>.observeValidations(
    viewModel: Validatable,
    dialogContext: Context = providedContext
) {
    viewModel.validationFailureEvent.observeEvent {
        when (it) {
            is ValidationFailureUi.Default -> {
                val level = it.level

                when {
                    level >= DefaultFailureLevel.ERROR -> errorDialog(dialogContext) {
                        setTitle(it.title)
                        setMessage(it.message)
                    }
                    level >= DefaultFailureLevel.WARNING -> warningDialog(
                        context = dialogContext,
                        onConfirm = it.confirmWarning
                    ) {
                        setTitle(it.title)
                        setMessage(it.message)
                    }
                }
            }
            is ValidationFailureUi.Custom -> displayDialogFor(it.payload)
        }
    }
}
