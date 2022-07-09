package com.edgeverse.wallet.feature_assets.presentation.balance.detail

import com.edgeverse.wallet.feature_assets.presentation.model.TokenModel
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountModel

class AssetDetailsModel(
    val token: TokenModel,
    val total: AmountModel,
    val transferable: AmountModel,
    val locked: AmountModel
)
