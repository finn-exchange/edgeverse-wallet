package com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond

import com.edgeverse.wallet.feature_staking_impl.domain.model.Unbonding

class UnboningsdState(
    val unbondings: List<Unbonding>,
    val anythingToRedeem: Boolean,
    val anythingToUnbond: Boolean
)
