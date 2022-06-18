package com.dfinn.wallet.feature_assets.domain.assets.filters

import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

object NonZeroBalanceFilter : AssetFilter {

    override val name: String = "NonZeroBalance"

    override fun shouldInclude(model: Asset) = model.total > BigDecimal.ZERO
}
