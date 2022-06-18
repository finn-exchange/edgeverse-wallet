package com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.filters

import com.dfinn.wallet.feature_staking_api.domain.model.Validator
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationFilter

object HasIdentityFilter : RecommendationFilter {

    override fun shouldInclude(model: Validator): Boolean {
        return model.identity != null
    }
}
