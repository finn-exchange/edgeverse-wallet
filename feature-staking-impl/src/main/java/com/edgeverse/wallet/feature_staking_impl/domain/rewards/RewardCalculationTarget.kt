package com.edgeverse.wallet.feature_staking_impl.domain.rewards

import java.math.BigDecimal
import java.math.BigInteger

class RewardCalculationTarget(
    val accountIdHex: String,
    val totalStake: BigInteger,
    val commission: BigDecimal
)
