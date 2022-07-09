package com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.ui

import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.setupImportTypeChooser
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherMixin

fun BaseFragment<*>.setupAddAccountLauncher(mixin: AddAccountLauncherMixin) {
    val asImportTypeChooser = object : ImportTypeChooserMixin {
        override val showChooserEvent = mixin.showImportTypeChooser
    }
    setupImportTypeChooser(asImportTypeChooser)

    mixin.showAddAccountTypeChooser.observeEvent {
        AddAccountChooserBottomSheet(
            context = requireContext(),
            payload = it
        ).show()
    }
}
