package com.edgeverse.wallet.feature_wallet_api.domain.implementations

import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.runtime.ext.utilityAsset
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.asset
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chainAsset
import com.edgeverse.wallet.runtime.state.selectedAssetFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class SharedStateTokenUseCase(
    private val tokenRepository: TokenRepository,
    private val sharedState: SingleAssetSharedState,
) : TokenUseCase {

    override suspend fun currentToken(): Token {
        val chainAsset = sharedState.chainAsset()

        return tokenRepository.getToken(chainAsset)
    }

    override fun currentTokenFlow(): Flow<Token> {
        return sharedState.selectedAssetFlow().flatMapLatest { chainAsset ->
            tokenRepository.observeToken(chainAsset)
        }
    }
}

class FixedTokenUseCase(
    private val tokenRepository: TokenRepository,
    private val chainId: ChainId,
    private val chainRegistry: ChainRegistry,
    private val chainAssetId: Int,
) : TokenUseCase {

    override suspend fun currentToken(): Token {
        val chainAsset = chainRegistry.asset(chainId, chainAssetId)

        return tokenRepository.getToken(chainAsset)
    }

    override fun currentTokenFlow(): Flow<Token> {
        return flow {
            val chainAsset = chainRegistry.asset(chainId, chainAssetId)

            emitAll(tokenRepository.observeToken(chainAsset))
        }
    }
}

class GenesisHashUtilityTokenUseCase(
    private val genesisHash: String,
    private val chainRegistry: ChainRegistry,
    private val tokenRepository: TokenRepository,
) : TokenUseCase {

    override suspend fun currentToken(): Token {
        return tokenRepository.getToken(getChainAsset())
    }

    override fun currentTokenFlow(): Flow<Token> {
        return flow {
            emitAll(tokenRepository.observeToken(getChainAsset()))
        }
    }

    private suspend fun getChainAsset(): Chain.Asset {
        return chainRegistry.getChain(genesisHash).utilityAsset
    }
}
