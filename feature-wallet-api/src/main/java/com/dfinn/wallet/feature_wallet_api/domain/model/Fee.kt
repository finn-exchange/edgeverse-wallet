package com.dfinn.wallet.feature_wallet_api.domain.model

import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

class Fee(
    val transferAmount: BigDecimal,
    val feeAmount: BigDecimal,
    val type: Chain.Asset
)
