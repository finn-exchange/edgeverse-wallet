package com.edgeverse.wallet.feature_assets.data.mappers.mappers

import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.common.utils.formatAsChange
import com.edgeverse.wallet.common.utils.formatAsCurrency
import com.edgeverse.wallet.common.utils.isNonNegative
import com.edgeverse.wallet.feature_assets.presentation.model.AssetModel
import com.edgeverse.wallet.feature_assets.presentation.model.TokenModel
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import java.math.BigDecimal

fun mapTokenToTokenModel(token: Token): TokenModel {
    return with(token) {
        val rateChange = token.recentRateChange

        val changeColorRes = when {
            rateChange == null -> R.color.gray2
            rateChange.isNonNegative -> R.color.green
            else -> R.color.red
        }

        TokenModel(
            configuration = configuration,
            dollarRate = (dollarRate ?: BigDecimal.ZERO).formatAsCurrency(),
            recentRateChange = (recentRateChange ?: BigDecimal.ZERO).formatAsChange(),
            rateChangeColorRes = changeColorRes
        )
    }
}

fun mapAssetToAssetModel(asset: Asset): AssetModel {
    return with(asset) {
        AssetModel(
            token = mapTokenToTokenModel(token),
            total = total,
            bonded = bonded,
            locked = locked,
            available = transferable,
            reserved = reserved,
            redeemable = redeemable,
            unbonding = unbonding,
            dollarAmount = dollarAmount
        )
    }
}
