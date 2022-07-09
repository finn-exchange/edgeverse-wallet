package com.edgeverse.wallet.common.mixin.impl

import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Browserable
import com.edgeverse.wallet.common.utils.showBrowser

fun <T> BaseFragment<T>.observeBrowserEvents(viewModel: T) where T : BaseViewModel, T : Browserable {
    viewModel.openBrowserEvent.observeEvent(this::showBrowser)
}
