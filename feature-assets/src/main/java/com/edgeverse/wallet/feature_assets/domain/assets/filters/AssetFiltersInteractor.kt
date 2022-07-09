package com.edgeverse.wallet.feature_assets.domain.assets.filters

import com.edgeverse.wallet.feature_assets.data.repository.assetFilters.AssetFiltersRepository
import kotlinx.coroutines.flow.first

class AssetFiltersInteractor(
    private val assetFiltersRepository: AssetFiltersRepository
) {

    val allFilters = assetFiltersRepository.allFilters

    fun updateFilters(filters: List<AssetFilter>) {
        assetFiltersRepository.updateAssetFilters(filters)
    }

    suspend fun currentFilters(): Set<AssetFilter> = assetFiltersRepository.assetFiltersFlow().first().toSet()
}
