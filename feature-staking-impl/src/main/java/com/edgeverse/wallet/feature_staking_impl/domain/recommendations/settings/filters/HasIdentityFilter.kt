package com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.filters

import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationFilter

object HasIdentityFilter : RecommendationFilter {

    override fun shouldInclude(model: Validator): Boolean {
        return model.identity != null
    }
}
