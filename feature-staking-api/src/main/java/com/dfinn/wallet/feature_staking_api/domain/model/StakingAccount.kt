package com.dfinn.wallet.feature_staking_api.domain.model

import com.dfinn.wallet.core.model.Network

class StakingAccount(
    val address: String,
    val name: String?,
    val network: Network
)
