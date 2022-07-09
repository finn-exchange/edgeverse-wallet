package com.edgeverse.wallet.root.navigation.nft

import com.edgeverse.wallet.R
import com.edgeverse.wallet.feature_nft_impl.NftRouter
import com.edgeverse.wallet.feature_nft_impl.presentation.nft.details.NftDetailsFragment
import com.edgeverse.wallet.root.navigation.BaseNavigator
import com.edgeverse.wallet.root.navigation.NavigationHolder

class NftNavigator(
    navigationHolder: NavigationHolder,
) : BaseNavigator(navigationHolder), NftRouter {

    override fun openNftDetails(nftId: String) = performNavigation(
        actionId = R.id.action_nftListFragment_to_nftDetailsFragment,
        args = NftDetailsFragment.getBundle(nftId)
    )
}
