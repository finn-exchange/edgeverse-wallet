package com.dfinn.wallet.root.navigation.dApp

import com.dfinn.wallet.R
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator.Response
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignExtrinsicFragment
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignPayload
import com.dfinn.wallet.root.navigation.BaseInterScreenCommunicator
import com.dfinn.wallet.root.navigation.NavigationHolder

class DAppSignCommunicatorImpl(navigationHolder: NavigationHolder) :
    BaseInterScreenCommunicator<DAppSignPayload, Response>(navigationHolder),
    DAppSignCommunicator {

    override fun openRequest(request: DAppSignPayload) {
        navController.navigate(R.id.action_DAppBrowserFragment_to_ConfirmSignExtrinsicFragment, DAppSignExtrinsicFragment.getBundle(request))
    }
}
