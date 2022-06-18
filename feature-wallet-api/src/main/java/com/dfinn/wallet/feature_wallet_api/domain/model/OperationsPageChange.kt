package com.dfinn.wallet.feature_wallet_api.domain.model

import com.dfinn.wallet.common.data.model.CursorPage

data class OperationsPageChange(
    val cursorPage: CursorPage<Operation>,
    val accountChanged: Boolean
)
