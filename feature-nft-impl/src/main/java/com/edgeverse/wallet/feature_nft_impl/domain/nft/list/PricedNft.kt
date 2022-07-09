package com.edgeverse.wallet.feature_nft_impl.domain.nft.list

import com.edgeverse.wallet.feature_nft_api.data.model.Nft
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token

class PricedNft(
    val nft: Nft,
    val nftPriceToken: Token?
)
