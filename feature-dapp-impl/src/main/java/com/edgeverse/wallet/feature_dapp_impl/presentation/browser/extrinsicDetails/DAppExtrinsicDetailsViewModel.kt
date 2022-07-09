package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter

class DAppExtrinsicDetailsViewModel(
    private val router: DAppRouter,
    val extrinsicContent: String
) : BaseViewModel() {

    fun closeClicked() {
        router.back()
    }
}
