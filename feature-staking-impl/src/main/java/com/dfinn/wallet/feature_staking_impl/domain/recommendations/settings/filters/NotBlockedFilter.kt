package com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.filters

import com.dfinn.wallet.feature_staking_api.domain.model.Validator
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationFilter

object NotBlockedFilter : RecommendationFilter {

    override fun shouldInclude(model: Validator) = model.prefs?.blocked?.not() ?: false
}
