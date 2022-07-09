package com.edgeverse.wallet.feature_wallet_api.domain.interfaces

import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    suspend fun observeTokens(chainAssets: List<Chain.Asset>): Flow<Map<Chain.Asset, Token>>

    suspend fun getToken(chainAsset: Chain.Asset): Token

    fun observeToken(chainAsset: Chain.Asset): Flow<Token>
}
