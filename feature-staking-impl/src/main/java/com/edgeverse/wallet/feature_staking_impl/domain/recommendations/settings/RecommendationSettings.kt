package com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings

import com.edgeverse.wallet.common.utils.Filter
import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import java.util.Comparator

typealias RecommendationFilter = Filter<Validator>
typealias RecommendationSorting = Comparator<Validator>
typealias RecommendationPostProcessor = (List<Validator>) -> List<Validator>

data class RecommendationSettings(
    val alwaysEnabledFilters: List<RecommendationFilter>,
    val customEnabledFilters: List<RecommendationFilter>,
    val postProcessors: List<RecommendationPostProcessor>,
    val sorting: RecommendationSorting,
    val limit: Int? = null
) {

    val allFilters = alwaysEnabledFilters + customEnabledFilters
}
