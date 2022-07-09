package com.edgeverse.wallet.feature_wallet_api.domain.model

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

class Fee(
    val transferAmount: BigDecimal,
    val feeAmount: BigDecimal,
    val type: Chain.Asset
)
