package com.edgeverse.wallet.feature_crowdloan_impl.presentation.model

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.createAddressIcon
import com.edgeverse.wallet.common.utils.images.Icon
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.ParachainMetadata

suspend fun generateCrowdloanIcon(
    parachainMetadata: ParachainMetadata?,
    depositorAddress: String,
    iconGenerator: AddressIconGenerator,
): Icon {
    return if (parachainMetadata != null) {
        Icon.FromLink(parachainMetadata.iconLink)
    } else {
        val icon = iconGenerator.createAddressIcon(depositorAddress, AddressIconGenerator.SIZE_BIG)

        return Icon.FromDrawable(icon)
    }
}
