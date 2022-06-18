package com.dfinn.wallet.common.mixin.impl

import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.api.Browserable
import com.dfinn.wallet.common.utils.showBrowser

fun <T> BaseFragment<T>.observeBrowserEvents(viewModel: T) where T : BaseViewModel, T : Browserable {
    viewModel.openBrowserEvent.observeEvent(this::showBrowser)
}
