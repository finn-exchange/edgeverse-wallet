package com.edgeverse.wallet.feature_assets.domain

import com.edgeverse.wallet.common.data.model.CursorPage
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TransactionFilter
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.model.Balances
import com.edgeverse.wallet.feature_wallet_api.domain.model.Operation
import com.edgeverse.wallet.feature_wallet_api.domain.model.OperationsPageChange
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow

interface WalletInteractor {

    fun balancesFlow(): Flow<Balances>

    suspend fun syncAssetsRates()

    suspend fun syncNfts(metaAccount: MetaAccount)

    fun assetFlow(chainId: ChainId, chainAssetId: Int): Flow<Asset>

    fun commissionAssetFlow(chainId: ChainId): Flow<Asset>

    suspend fun getCurrentAsset(chainId: ChainId, chainAssetId: Int): Asset

    fun operationsFirstPageFlow(chainId: ChainId, chainAssetId: Int): Flow<OperationsPageChange>

    suspend fun syncOperationsFirstPage(
        chainId: ChainId,
        chainAssetId: Int,
        pageSize: Int,
        filters: Set<TransactionFilter>,
    ): Result<*>

    suspend fun getOperations(
        chainId: ChainId,
        chainAssetId: Int,
        pageSize: Int,
        cursor: String?,
        filters: Set<TransactionFilter>
    ): Result<CursorPage<Operation>>
}
