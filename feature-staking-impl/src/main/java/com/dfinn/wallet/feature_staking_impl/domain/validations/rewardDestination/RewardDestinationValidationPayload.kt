package com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination

import com.dfinn.wallet.feature_staking_api.domain.model.StakingState
import java.math.BigDecimal

class RewardDestinationValidationPayload(
    val availableControllerBalance: BigDecimal,
    val fee: BigDecimal,
    val stashState: StakingState.Stash
)
