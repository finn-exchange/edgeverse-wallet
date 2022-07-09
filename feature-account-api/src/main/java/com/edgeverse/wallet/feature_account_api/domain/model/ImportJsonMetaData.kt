package com.edgeverse.wallet.feature_account_api.domain.model

import com.edgeverse.wallet.core.model.CryptoType

class ImportJsonMetaData(
    val name: String?,
    val chainId: String?,
    val encryptionType: CryptoType
)
