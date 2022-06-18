package com.dfinn.wallet.root.navigation.dApp

import com.dfinn.wallet.R
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator.Response
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DappSearchFragment
import com.dfinn.wallet.feature_dapp_impl.presentation.search.SearchPayload
import com.dfinn.wallet.root.navigation.BaseInterScreenCommunicator
import com.dfinn.wallet.root.navigation.NavigationHolder

class DAppSearchCommunicatorImpl(navigationHolder: NavigationHolder) :
    BaseInterScreenCommunicator<SearchPayload, Response>(navigationHolder),
    DAppSearchCommunicator {
    override fun openRequest(request: SearchPayload) {
        navController.navigate(R.id.action_DAppBrowserFragment_to_dappSearchFragment, DappSearchFragment.getBundle(request))
    }
}
