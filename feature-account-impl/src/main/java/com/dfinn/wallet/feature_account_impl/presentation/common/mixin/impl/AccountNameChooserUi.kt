package com.dfinn.wallet.feature_account_impl.presentation.common.mixin.impl

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.utils.onTextChanged
import com.dfinn.wallet.common.utils.setVisible
import com.dfinn.wallet.common.view.InputField
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.api.WithAccountNameChooserMixin

fun <V> BaseFragment<V>.setupAccountNameChooserUi(
    viewModel: V,
    ui: InputField,
    additionalViewsToControlVisibility: List<View> = emptyList(),
) where V : BaseViewModel, V : WithAccountNameChooserMixin {
    setupAccountNameChooserUi(viewModel, ui, viewLifecycleOwner, additionalViewsToControlVisibility)
}

fun setupAccountNameChooserUi(
    viewModel: WithAccountNameChooserMixin,
    ui: InputField,
    owner: LifecycleOwner,
    additionalViewsToControlVisibility: List<View> = emptyList(),
) {
    ui.content.onTextChanged {
        viewModel.accountNameChooser.nameChanged(it)
    }

    viewModel.accountNameChooser.nameState.observe(owner) { state ->
        val isVisible = state is AccountNameChooserMixin.State.Input

        ui.setVisible(isVisible)
        additionalViewsToControlVisibility.forEach { it.setVisible(isVisible) }

        if (state is AccountNameChooserMixin.State.Input) {
            if (state.value != ui.content.text.toString()) {
                ui.content.setText(state.value)
            }
        }
    }
}
