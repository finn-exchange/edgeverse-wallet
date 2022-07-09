package com.edgeverse.wallet.root.navigation.dApp

import com.edgeverse.wallet.R
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator.Response
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DappSearchFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.SearchPayload
import com.edgeverse.wallet.root.navigation.BaseInterScreenCommunicator
import com.edgeverse.wallet.root.navigation.NavigationHolder

class DAppSearchCommunicatorImpl(navigationHolder: NavigationHolder) :
    BaseInterScreenCommunicator<SearchPayload, Response>(navigationHolder),
    DAppSearchCommunicator {
    override fun openRequest(request: SearchPayload) {
        navController.navigate(R.id.action_DAppBrowserFragment_to_dappSearchFragment, DappSearchFragment.getBundle(request))
    }
}
