package com.dfinn.wallet.feature_staking_impl.presentation.confirm.hints

import com.dfinn.wallet.common.mixin.hints.ConstantHintsMixin
import com.dfinn.wallet.common.mixin.hints.HintsMixin
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingProcess.ReadyToSubmit.Payload
import com.dfinn.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import kotlinx.coroutines.CoroutineScope

class ConfirmStakeHintsMixinFactory(
    private val interactor: StakingInteractor,
    private val resourceManager: ResourceManager,
    private val stakingHintsUseCase: StakingHintsUseCase,
) {

    fun create(
        coroutineScope: CoroutineScope,
        payload: Payload
    ): HintsMixin = ConfirmStakeHintsMixin(
        interactor = interactor,
        resourceManager = resourceManager,
        payload = payload,
        coroutineScope = coroutineScope,
        stakingHintsUseCase = stakingHintsUseCase
    )
}

private class ConfirmStakeHintsMixin(
    private val interactor: StakingInteractor,
    private val resourceManager: ResourceManager,
    private val payload: Payload,
    private val stakingHintsUseCase: StakingHintsUseCase,
    coroutineScope: CoroutineScope
) : ConstantHintsMixin(coroutineScope) {

    override suspend fun getHints(): List<String> {

        return when (payload) {
            is Payload.Validators -> changeValidatorsHints()
            else -> beginStakeHints()
        }
    }

    private suspend fun beginStakeHints(): List<String> = listOf(
        rewardPeriodHint(),
        stakingHintsUseCase.noRewardDurationUnstakingHint(),
        stakingHintsUseCase.redeemHint(),
        stakingHintsUseCase.unstakingDurationHint(),
    )

    private fun changeValidatorsHints(): List<String> = listOf(
        validatorsChangeHint()
    )

    private fun validatorsChangeHint(): String {
        return resourceManager.getString(R.string.staking_your_validators_changing_title)
    }

    private suspend fun rewardPeriodHint(): String {
        val hours = interactor.getEraHoursLength()

        return resourceManager.getString(
            R.string.staking_hint_rewards_format_v2_2_0,
            resourceManager.getQuantityString(R.plurals.common_hours_format, hours, hours)
        )
    }
}
