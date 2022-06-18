package com.dfinn.wallet.feature_dapp_impl

import com.dfinn.wallet.feature_dapp_impl.presentation.addToFavourites.AddToFavouritesPayload

interface DAppRouter {

    fun openChangeAccount()

    fun openDAppBrowser(initialUrl: String)

    fun openDappSearch()

    fun openAddToFavourites(payload: AddToFavouritesPayload)

    fun openExtrinsicDetails(extrinsicContent: String)

    fun openAuthorizedDApps()

    fun back()
}
