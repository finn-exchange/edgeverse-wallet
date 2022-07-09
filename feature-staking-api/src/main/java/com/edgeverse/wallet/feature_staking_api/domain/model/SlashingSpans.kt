package com.edgeverse.wallet.feature_staking_api.domain.model

class SlashingSpans(
    val lastNonZeroSlash: EraIndex,
    val prior: List<EraIndex>
)
