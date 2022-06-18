package com.dfinn.wallet.common.mixin.impl

import android.content.Context
import com.dfinn.wallet.common.base.BaseFragmentMixin
import com.dfinn.wallet.common.mixin.api.Validatable
import com.dfinn.wallet.common.mixin.api.ValidationFailureUi
import com.dfinn.wallet.common.validation.DefaultFailureLevel
import com.dfinn.wallet.common.view.dialog.errorDialog
import com.dfinn.wallet.common.view.dialog.warningDialog

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
