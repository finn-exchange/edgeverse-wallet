package com.dfinn.wallet.feature_wallet_api.domain

import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface AssetUseCase {

    fun currentAssetFlow(): Flow<Asset>

    suspend fun availableAssetsToSelect(): List<Asset>
}

suspend fun AssetUseCase.getCurrentAsset() = currentAssetFlow().first()
