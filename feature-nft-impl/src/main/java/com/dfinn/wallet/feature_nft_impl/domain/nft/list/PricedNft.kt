package com.dfinn.wallet.feature_nft_impl.domain.nft.list

import com.dfinn.wallet.feature_nft_api.data.model.Nft
import com.dfinn.wallet.feature_wallet_api.domain.model.Token

class PricedNft(
    val nft: Nft,
    val nftPriceToken: Token?
)
