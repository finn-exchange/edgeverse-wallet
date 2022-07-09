package com.edgeverse.wallet.feature_wallet_api.domain

import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenUseCase {

    suspend fun currentToken(): Token

    fun currentTokenFlow(): Flow<Token>
}
