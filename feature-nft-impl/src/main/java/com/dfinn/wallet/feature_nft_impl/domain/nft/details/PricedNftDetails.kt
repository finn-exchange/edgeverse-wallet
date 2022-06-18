package com.dfinn.wallet.feature_nft_impl.domain.nft.details

import com.dfinn.wallet.feature_nft_api.data.model.NftDetails
import com.dfinn.wallet.feature_wallet_api.domain.model.Price

class PricedNftDetails(
    val nftDetails: NftDetails,
    val price: Price?
)
