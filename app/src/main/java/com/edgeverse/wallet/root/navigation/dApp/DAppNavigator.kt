package com.edgeverse.wallet.root.navigation.dApp

import com.edgeverse.wallet.R
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountListFragment
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites.AddToFavouritesFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites.AddToFavouritesPayload
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.DappExtrinsicDetailsFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main.DAppBrowserFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DappSearchFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.SearchPayload
import com.edgeverse.wallet.root.navigation.BaseNavigator
import com.edgeverse.wallet.root.navigation.NavigationHolder

class DAppNavigator(
    navigationHolder: NavigationHolder,
) : BaseNavigator(navigationHolder), DAppRouter {

    override fun openChangeAccount() = performNavigation(
        actionId = R.id.action_open_accounts,
        args = AccountListFragment.getBundle(AccountChosenNavDirection.BACK)
    )

    override fun openDAppBrowser(initialUrl: String) = performNavigation(
        cases = arrayOf(
            R.id.mainFragment to R.id.action_mainFragment_to_dappBrowserGraph,
            R.id.dappSearchFragment to R.id.action_dappSearchFragment_to_dapp_browser_graph,
        ),
        args = DAppBrowserFragment.getBundle(initialUrl)
    )

    override fun openDappSearch() = performNavigation(
        actionId = R.id.action_mainFragment_to_dappSearchGraph,
        args = DappSearchFragment.getBundle(SearchPayload(initialUrl = null))
    )

    override fun openAddToFavourites(payload: AddToFavouritesPayload) = performNavigation(
        actionId = R.id.action_DAppBrowserFragment_to_addToFavouritesFragment,
        args = AddToFavouritesFragment.getBundle(payload)
    )

    override fun openExtrinsicDetails(extrinsicContent: String) = performNavigation(
        actionId = R.id.action_ConfirmSignExtrinsicFragment_to_extrinsicDetailsFragment,
        args = DappExtrinsicDetailsFragment.getBundle(extrinsicContent)
    )

    override fun openAuthorizedDApps() = performNavigation(
        actionId = R.id.action_mainFragment_to_authorizedDAppsFragment
    )
}
