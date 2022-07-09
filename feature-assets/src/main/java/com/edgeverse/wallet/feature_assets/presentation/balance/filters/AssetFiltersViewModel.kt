package com.edgeverse.wallet.feature_assets.presentation.balance.filters

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.checkEnabled
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_assets.domain.assets.filters.AssetFiltersInteractor
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AssetFiltersViewModel(
    private val interactor: AssetFiltersInteractor,
    private val router: WalletRouter,
) : BaseViewModel() {

    private val initialFilters = flowOf { interactor.currentFilters() }
        .inBackground()
        .share()

    val filtersEnabledMap = createFilterEnabledMap()

    init {
        applyInitialState()
    }

    fun backClicked() {
        router.back()
    }

    fun applyClicked() = launch {
        val filters = interactor.allFilters.filter(filtersEnabledMap::checkEnabled)

        interactor.updateFilters(filters)

        router.back()
    }

    private fun applyInitialState() = launch {
        val initialFilters = initialFilters.first()

        filtersEnabledMap.forEach { (filter, checked) ->
            checked.value = filter in initialFilters
        }
    }

    private fun createFilterEnabledMap() = interactor.allFilters.associateWith { MutableStateFlow(false) }
}
