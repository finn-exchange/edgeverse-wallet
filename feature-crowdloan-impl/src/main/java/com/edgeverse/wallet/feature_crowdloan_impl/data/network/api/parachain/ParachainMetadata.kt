package com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parachain

import com.edgeverse.wallet.feature_crowdloan_api.data.repository.ParachainMetadata

fun mapParachainMetadataRemoteToParachainMetadata(parachainMetadata: ParachainMetadataRemote) =
    with(parachainMetadata) {
        ParachainMetadata(
            iconLink = icon,
            name = name,
            description = description,
            rewardRate = rewardRate?.toBigDecimal(),
            website = website,
            customFlow = customFlow,
            token = token,
            extras = extras.orEmpty()
        )
    }
