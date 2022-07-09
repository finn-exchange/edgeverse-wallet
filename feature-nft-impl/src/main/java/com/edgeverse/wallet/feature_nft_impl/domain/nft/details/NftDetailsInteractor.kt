package com.edgeverse.wallet.feature_nft_impl.domain.nft.details

import com.edgeverse.wallet.feature_nft_api.data.repository.NftRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.model.Price
import com.edgeverse.wallet.runtime.ext.utilityAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class NftDetailsInteractor(
    private val nftRepository: NftRepository,
    private val tokenRepository: TokenRepository
) {

    fun nftDetailsFlow(nftIdentifier: String): Flow<PricedNftDetails> {
        return nftRepository.nftDetails(nftIdentifier).flatMapLatest { nftDetails ->
            tokenRepository.observeToken(nftDetails.chain.utilityAsset).map { token ->
                PricedNftDetails(
                    nftDetails = nftDetails,
                    price = nftDetails.price?.let {
                        Price(
                            amount = it,
                            token = token
                        )
                    }
                )
            }
        }
    }
}
