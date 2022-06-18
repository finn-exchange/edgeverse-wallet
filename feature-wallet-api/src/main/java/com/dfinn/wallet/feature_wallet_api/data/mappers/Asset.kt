package com.dfinn.wallet.feature_wallet_api.data.mappers

import androidx.annotation.StringRes
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_wallet_api.R
import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import com.dfinn.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount
import com.dfinn.wallet.feature_wallet_api.presentation.model.AssetModel
import java.math.BigDecimal

fun mapAssetToAssetModel(
    asset: Asset,
    resourceManager: ResourceManager,
    retrieveAmount: (Asset) -> BigDecimal = Asset::transferable,
    @StringRes patternId: Int? = R.string.common_available_format
): AssetModel {
    val amount = retrieveAmount(asset).formatTokenAmount(asset.token.configuration)
    val formattedAmount = patternId?.let { resourceManager.getString(patternId, amount) } ?: amount

    return with(asset) {
        AssetModel(
            asset.token.configuration.chainId,
            asset.token.configuration.id,
            token.configuration.iconUrl,
            token.configuration.symbol,
            formattedAmount
        )
    }
}
