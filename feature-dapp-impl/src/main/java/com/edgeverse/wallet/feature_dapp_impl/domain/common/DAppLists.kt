package com.edgeverse.wallet.feature_dapp_impl.domain.common

import com.edgeverse.wallet.common.utils.mapToSet
import com.edgeverse.wallet.feature_dapp_api.data.model.DApp
import com.edgeverse.wallet.feature_dapp_api.data.model.DappMetadata
import com.edgeverse.wallet.feature_dapp_impl.data.mappers.mapDappCategoriesToDescription
import com.edgeverse.wallet.feature_dapp_impl.data.model.FavouriteDApp

fun createDAppComparator() = compareByDescending<DApp> { it.isFavourite }
    .thenBy { it.name }

// Build mapping in O(Metadatas + Favourites) in case of HashMap. It allows constant time access later
@OptIn(ExperimentalStdlibApi::class)
internal fun buildUrlToDappMapping(
    dAppMetadatas: List<DappMetadata>,
    favourites: List<FavouriteDApp>
): Map<String, DApp> {
    val favouritesUrls: Set<String> = favourites.mapToSet { it.url }

    return buildMap {
        val fromFavourites = favourites.associateBy(
            keySelector = { it.url },
            valueTransform = ::favouriteToDApp
        )
        putAll(fromFavourites)

        // overlapping metadata urls will override favourites in the map and thus use metadata for display
        val fromMetadatas = dAppMetadatas.associateBy(
            keySelector = { it.url },
            valueTransform = { dAppMetadataToDApp(it, isFavourite = it.url in favouritesUrls) }
        )
        putAll(fromMetadatas)
    }
}

private fun favouriteToDApp(favouriteDApp: FavouriteDApp): DApp {
    return DApp(
        name = favouriteDApp.label,
        description = favouriteDApp.url,
        iconLink = favouriteDApp.icon,
        url = favouriteDApp.url,
        isFavourite = true
    )
}

private fun dAppMetadataToDApp(metadata: DappMetadata, isFavourite: Boolean): DApp {
    return DApp(
        name = metadata.name,
        description = mapDappCategoriesToDescription(metadata.categories),
        iconLink = metadata.iconLink,
        url = metadata.url,
        isFavourite = isFavourite
    )
}