package com.edgeverse.wallet.feature_assets.presentation.transaction.history

import com.edgeverse.wallet.feature_assets.presentation.transaction.history.mixin.TransactionHistoryUi.State

fun TransferHistorySheet.showState(state: State) {
    when (state.listState) {
        is State.ListState.Empty -> showPlaceholder()
        is State.ListState.EmptyProgress -> showProgress()
        is State.ListState.Data -> showTransactions(state.listState.items)
    }

    setFiltersVisible(state.filtersButtonVisible)
}
