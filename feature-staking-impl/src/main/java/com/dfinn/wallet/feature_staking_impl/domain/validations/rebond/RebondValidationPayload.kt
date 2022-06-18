package com.dfinn.wallet.feature_staking_impl.domain.validations.rebond

import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

class RebondValidationPayload(
    val controllerAsset: Asset,
    val fee: BigDecimal,
    val rebondAmount: BigDecimal
)
