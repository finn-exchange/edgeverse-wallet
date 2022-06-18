package com.dfinn.wallet.feature_assets.domain.assets.list

import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_nft_api.data.model.Nft
import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

private const val PREVIEW_COUNT = 3

class AssetsListInteractor(
    private val accountRepository: AccountRepository,
    private val nftRepository: NftRepository
) {

    suspend fun fullSyncNft(nft: Nft) = nftRepository.fullNftSync(nft)

    fun observeNftPreviews(): Flow<NftPreviews> {
        return accountRepository.selectedMetaAccountFlow()
            .flatMapLatest(nftRepository::allNftFlow)
            .map { nfts ->
                NftPreviews(
                    totalNftsCount = nfts.size,
                    nftPreviews = nfts.take(PREVIEW_COUNT)
                )
            }
    }
}
