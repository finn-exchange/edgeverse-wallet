package com.edgeverse.wallet.feature_staking_impl.domain.validations.bond

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

class BondMoreValidationPayload(
    val stashAddress: String,
    val fee: BigDecimal,
    val amount: BigDecimal,
    val chainAsset: Chain.Asset,
)
