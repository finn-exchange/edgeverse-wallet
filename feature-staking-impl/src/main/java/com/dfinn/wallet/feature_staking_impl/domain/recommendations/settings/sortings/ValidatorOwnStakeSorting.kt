package com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.sortings

import com.dfinn.wallet.feature_staking_api.domain.model.Validator
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSorting
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.notElected

object ValidatorOwnStakeSorting : RecommendationSorting by Comparator.comparing({ validator: Validator ->
    validator.electedInfo?.ownStake ?: notElected(validator.accountIdHex)
}).reversed()
