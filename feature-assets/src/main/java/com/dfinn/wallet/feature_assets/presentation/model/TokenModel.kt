package com.dfinn.wallet.feature_assets.presentation.model

import androidx.annotation.ColorRes
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain

data class TokenModel(
    val configuration: Chain.Asset,
    val dollarRate: String,
    val recentRateChange: String,
    @ColorRes val rateChangeColorRes: Int
)
