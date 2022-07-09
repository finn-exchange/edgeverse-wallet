package com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.impl

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.onTextChanged
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.view.InputField
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.WithAccountNameChooserMixin

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
