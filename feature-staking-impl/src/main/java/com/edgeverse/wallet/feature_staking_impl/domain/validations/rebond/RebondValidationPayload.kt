package com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond

import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

class RebondValidationPayload(
    val controllerAsset: Asset,
    val fee: BigDecimal,
    val rebondAmount: BigDecimal
)
