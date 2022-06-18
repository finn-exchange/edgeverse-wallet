package com.dfinn.wallet.feature_account_api.domain.model

import com.dfinn.wallet.core.model.CryptoType

data class PreferredCryptoType(
    val cryptoType: CryptoType,
    val frozen: Boolean
)
