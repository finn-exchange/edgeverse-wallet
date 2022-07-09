package com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.filters

import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationFilter

object NotBlockedFilter : RecommendationFilter {

    override fun shouldInclude(model: Validator) = model.prefs?.blocked?.not() ?: false
}
