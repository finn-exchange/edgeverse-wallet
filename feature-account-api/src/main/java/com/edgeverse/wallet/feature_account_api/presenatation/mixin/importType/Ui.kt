package com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType

import com.edgeverse.wallet.common.base.BaseFragment

fun BaseFragment<*>.setupImportTypeChooser(mixin: ImportTypeChooserMixin) {
    mixin.showChooserEvent.observeEvent {
        ImportTypeChooserBottomSheet(
            context = requireContext(),
            onChosen = it.onChosen,
            allowedSources = it.allowedTypes
        ).show()
    }
}
