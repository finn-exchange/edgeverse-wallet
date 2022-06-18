package com.dfinn.wallet.feature_wallet_impl.data.repository

import com.dfinn.wallet.core_db.dao.TokenDao
import com.dfinn.wallet.core_db.model.TokenLocal
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.dfinn.wallet.feature_wallet_api.domain.model.Token
import com.dfinn.wallet.feature_wallet_impl.data.mappers.mapTokenLocalToToken
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TokenRepositoryImpl(
    private val tokenDao: TokenDao
) : TokenRepository {

    override suspend fun observeTokens(chainAssets: List<Chain.Asset>): Flow<Map<Chain.Asset, Token>> {
        val symbols = chainAssets.map { it.symbol }.distinct()

        return tokenDao.observeTokens(symbols).map { tokens ->
            val tokensBySymbol = tokens.associateBy { it.symbol }

            chainAssets.associateWith {
                mapTokenLocalToToken(tokensBySymbol.getValue(it.symbol), it)
            }
        }
    }

    override suspend fun getToken(chainAsset: Chain.Asset): Token = withContext(Dispatchers.Default) {
        val tokenLocal = tokenDao.getToken(chainAsset.symbol) ?: TokenLocal.createEmpty(chainAsset.symbol)

        mapTokenLocalToToken(tokenLocal, chainAsset)
    }

    override fun observeToken(chainAsset: Chain.Asset): Flow<Token> {
        return tokenDao.observeToken(chainAsset.symbol)
            .map {
                mapTokenLocalToToken(it, chainAsset)
            }
    }
}
