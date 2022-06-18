package com.dfinn.wallet.feature_staking_impl.domain.validations.bond

import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

class BondMoreValidationPayload(
    val stashAddress: String,
    val fee: BigDecimal,
    val amount: BigDecimal,
    val chainAsset: Chain.Asset,
)
