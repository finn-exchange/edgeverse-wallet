package com.edgeverse.wallet.feature_assets.presentation.transaction.filter

import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.checkEnabled
import com.edgeverse.wallet.common.utils.filterToSet
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.invoke
import com.edgeverse.wallet.common.utils.lazyAsync
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TransactionFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TransactionHistoryFilterViewModel(
    private val router: WalletRouter,
    private val historyFiltersProviderFactory: HistoryFiltersProviderFactory
) : BaseViewModel() {

    private val historyFiltersProvider by lazyAsync {
        historyFiltersProviderFactory.get(router.currentStackEntryLifecycle)
    }

    private val initialFiltersFlow = flow { emit(historyFiltersProvider().currentFilters()) }
        .share()

    val filtersEnabledMap by lazyAsync {
        createFilterEnabledMap()
    }

    private val modifiedFilters = flow {
        val inner = combine(filtersEnabledMap().values) {
            historyFiltersProvider().allFilters.filterToSet {
                filtersEnabledMap().checkEnabled(it)
            }
        }

        emitAll(inner)
    }
        .inBackground()
        .share()

    val isApplyButtonEnabled = combine(initialFiltersFlow, modifiedFilters) { initial, modified ->
        initial != modified && modified.isNotEmpty()
    }.share()

    init {
        viewModelScope.launch {
            initFromState(initialFiltersFlow.first())
        }
    }

    private fun initFromState(currentState: Set<TransactionFilter>) = launch {
        filtersEnabledMap().forEach { (filter, checked) ->
            checked.value = filter in currentState
        }
    }

    fun resetFilter() {
        viewModelScope.launch {
            val defaultFilters = historyFiltersProvider().defaultFilters

            initFromState(defaultFilters)
        }
    }

    fun backClicked() {
        router.back()
    }

    private suspend fun createFilterEnabledMap() = historyFiltersProvider().allFilters.associateWith { MutableStateFlow(true) }

    fun applyClicked() {
        viewModelScope.launch {
            historyFiltersProvider().setCustomFilters(modifiedFilters.first())

            router.back()
        }
    }
}
