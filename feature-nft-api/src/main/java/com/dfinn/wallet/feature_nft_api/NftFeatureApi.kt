package com.dfinn.wallet.feature_nft_api

import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository

interface NftFeatureApi {

    val nftRepository: NftRepository
}
