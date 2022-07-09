package com.edgeverse.wallet.feature_dapp_impl.data.mappers

import com.edgeverse.wallet.core_db.model.FavouriteDAppLocal
import com.edgeverse.wallet.feature_dapp_impl.data.model.FavouriteDApp

fun mapFavouriteDAppLocalToFavouriteDApp(favouriteDAppLocal: FavouriteDAppLocal): FavouriteDApp {
    return with(favouriteDAppLocal) {
        FavouriteDApp(
            url = url,
            label = label,
            icon = icon
        )
    }
}

fun mapFavouriteDAppToFavouriteDAppLocal(favouriteDApp: FavouriteDApp): FavouriteDAppLocal {
    return with(favouriteDApp) {
        FavouriteDAppLocal(
            url = url,
            label = label,
            icon = icon
        )
    }
}
