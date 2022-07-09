package com.edgeverse.wallet.feature_account_api.domain.model

import com.edgeverse.wallet.core.model.CryptoType

data class PreferredCryptoType(
    val cryptoType: CryptoType,
    val frozen: Boolean
)
