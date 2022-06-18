package com.dfinn.wallet.feature_account_api.domain.model

import com.dfinn.wallet.core.model.CryptoType

class ImportJsonMetaData(
    val name: String?,
    val chainId: String?,
    val encryptionType: CryptoType
)
