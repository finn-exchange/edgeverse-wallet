package com.dfinn.wallet.feature_nft_impl.data.source

import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import com.dfinn.wallet.feature_nft_api.data.model.Nft
import com.dfinn.wallet.feature_nft_api.data.model.NftDetails
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow

interface NftProvider {

    suspend fun initialNftsSync(chain: Chain, metaAccount: MetaAccount, forceOverwrite: Boolean)

    suspend fun nftFullSync(nft: Nft)

    fun nftDetailsFlow(nftIdentifier: String): Flow<NftDetails>
}
