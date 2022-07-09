package com.edgeverse.wallet.feature_nft_impl.presentation.nft.common

import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.format
import com.edgeverse.wallet.feature_nft_api.data.model.Nft
import com.edgeverse.wallet.nova.feature_nft_impl.R

fun ResourceManager.formatIssuance(issuance: Nft.Issuance): String {
    return when (issuance) {
        is Nft.Issuance.Unlimited -> getString(R.string.nft_issuance_unlimited)

        is Nft.Issuance.Limited -> {
            getString(
                R.string.nft_issuance_limited_format,
                issuance.edition.format(), issuance.max.format()
            )
        }
    }
}
