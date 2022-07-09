package com.edgeverse.wallet.root.navigation.dApp

import com.edgeverse.wallet.R
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator.Response
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignExtrinsicFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignPayload
import com.edgeverse.wallet.root.navigation.BaseInterScreenCommunicator
import com.edgeverse.wallet.root.navigation.NavigationHolder

class DAppSignCommunicatorImpl(navigationHolder: NavigationHolder) :
    BaseInterScreenCommunicator<DAppSignPayload, Response>(navigationHolder),
    DAppSignCommunicator {

    override fun openRequest(request: DAppSignPayload) {
        navController.navigate(R.id.action_DAppBrowserFragment_to_ConfirmSignExtrinsicFragment, DAppSignExtrinsicFragment.getBundle(request))
    }
}
