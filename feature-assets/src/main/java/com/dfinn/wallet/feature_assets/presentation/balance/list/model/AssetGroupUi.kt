package com.dfinn.wallet.feature_assets.presentation.balance.list.model

import com.dfinn.wallet.feature_account_api.presenatation.chain.ChainUi

data class AssetGroupUi(
    val chainUi: ChainUi,
    val groupBalanceFiat: String
)
