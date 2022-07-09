package com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.sortings

import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSorting
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.notElected

object TotalStakeSorting : RecommendationSorting by Comparator.comparing({ validator: Validator ->
    validator.electedInfo?.totalStake ?: notElected(validator.accountIdHex)
}).reversed()
