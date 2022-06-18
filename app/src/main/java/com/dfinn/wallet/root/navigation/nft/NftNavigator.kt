package com.dfinn.wallet.root.navigation.nft

import com.dfinn.wallet.R
import com.dfinn.wallet.feature_nft_impl.NftRouter
import com.dfinn.wallet.feature_nft_impl.presentation.nft.details.NftDetailsFragment
import com.dfinn.wallet.root.navigation.BaseNavigator
import com.dfinn.wallet.root.navigation.NavigationHolder

class NftNavigator(
    navigationHolder: NavigationHolder,
) : BaseNavigator(navigationHolder), NftRouter {

    override fun openNftDetails(nftId: String) = performNavigation(
        actionId = R.id.action_nftListFragment_to_nftDetailsFragment,
        args = NftDetailsFragment.getBundle(nftId)
    )
}
