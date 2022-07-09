package com.edgeverse.wallet.feature_staking_impl.domain.validations.reedeem

import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

class RedeemValidationPayload(
    val fee: BigDecimal,
    val asset: Asset
)
