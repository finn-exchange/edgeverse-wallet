package com.edgeverse.wallet.feature_nft_api.data.repository

import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_nft_api.data.model.Nft
import com.edgeverse.wallet.feature_nft_api.data.model.NftDetails
import kotlinx.coroutines.flow.Flow

interface NftRepository {

    fun allNftFlow(metaAccount: MetaAccount): Flow<List<Nft>>

    fun nftDetails(nftId: String): Flow<NftDetails>

    suspend fun initialNftSync(metaAccount: MetaAccount, forceOverwrite: Boolean)

    suspend fun fullNftSync(nft: Nft)
}
