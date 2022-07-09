package com.edgeverse.wallet.feature_nft_impl.presentation.nft.list

import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountModel

data class NftListItem(
    val content: LoadingState<Content>,
    val identifier: String,
) {

    data class Content(
        val issuance: String,
        val title: String,
        val price: AmountModel?,
        val media: String?,
    )
}
