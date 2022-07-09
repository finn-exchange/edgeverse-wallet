package com.edgeverse.wallet.feature_assets.presentation.balance.list.model

import com.edgeverse.wallet.feature_account_api.presenatation.chain.ChainUi

data class AssetGroupUi(
    val chainUi: ChainUi,
    val groupBalanceFiat: String
)
