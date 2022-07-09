package com.edgeverse.wallet.feature_wallet_api.domain.model

import com.edgeverse.wallet.common.data.model.CursorPage

data class OperationsPageChange(
    val cursorPage: CursorPage<Operation>,
    val accountChanged: Boolean
)
