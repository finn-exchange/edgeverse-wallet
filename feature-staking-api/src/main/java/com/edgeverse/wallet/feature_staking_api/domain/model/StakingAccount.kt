package com.edgeverse.wallet.feature_staking_api.domain.model

import com.edgeverse.wallet.core.model.Network

class StakingAccount(
    val address: String,
    val name: String?,
    val network: Network
)
