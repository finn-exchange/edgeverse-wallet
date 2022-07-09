package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.hints

import com.edgeverse.wallet.common.mixin.hints.ConstantHintsMixin
import com.edgeverse.wallet.common.mixin.hints.HintsMixin
import com.edgeverse.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import kotlinx.coroutines.CoroutineScope

class UnbondHintsMixinFactory(
    private val stakingHintsUseCase: StakingHintsUseCase,
) {

    fun create(coroutineScope: CoroutineScope,): HintsMixin = UnbondHintsMixin(
        coroutineScope = coroutineScope,
        stakingHintsUseCase = stakingHintsUseCase
    )
}

private class UnbondHintsMixin(
    coroutineScope: CoroutineScope,
    private val stakingHintsUseCase: StakingHintsUseCase,
) : ConstantHintsMixin(coroutineScope) {

    override suspend fun getHints(): List<String> = listOf(
        stakingHintsUseCase.unstakingDurationHint(),
        stakingHintsUseCase.noRewardDurationUnstakingHint(),
        stakingHintsUseCase.redeemHint(),
    )
}
