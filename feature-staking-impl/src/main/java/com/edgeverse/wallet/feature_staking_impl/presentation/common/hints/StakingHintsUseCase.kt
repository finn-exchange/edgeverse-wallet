package com.edgeverse.wallet.feature_staking_impl.presentation.common.hints

import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor

class StakingHintsUseCase(
    private val resourceManager: ResourceManager,
    private val stakingInteractor: StakingInteractor,
) {

    fun redeemHint(): String {
        return resourceManager.getString(R.string.staking_hint_redeem_v2_2_0)
    }

    suspend fun unstakingDurationHint(): String {
        val lockupPeriod = stakingInteractor.getLockupPeriodInDays()

        return resourceManager.getString(
            R.string.staking_hint_unstake_format_v2_2_0,
            resourceManager.getQuantityString(R.plurals.staking_main_lockup_period_value, lockupPeriod, lockupPeriod)
        )
    }

    fun noRewardDurationUnstakingHint(): String {
        return resourceManager.getString(R.string.staking_hint_no_rewards_v2_2_0)
    }
}
