package com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond

import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

data class UnbondValidationPayload(
    val stash: StakingState.Stash,
    val fee: BigDecimal,
    val amount: BigDecimal,
    val asset: Asset,
)
