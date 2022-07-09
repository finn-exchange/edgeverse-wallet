package com.edgeverse.wallet.feature_staking_impl.domain.recommendations

import androidx.lifecycle.Lifecycle
import com.edgeverse.wallet.common.data.memory.ComputationalCache
import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validators.ValidatorProvider
import com.edgeverse.wallet.feature_staking_impl.domain.validators.ValidatorSource
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val ELECTED_VALIDATORS_CACHE = "ELECTED_VALIDATORS_CACHE"

class ValidatorRecommendatorFactory(
    private val validatorProvider: ValidatorProvider,
    private val sharedState: StakingSharedState,
    private val computationalCache: ComputationalCache
) {

    suspend fun awaitValidatorLoading(lifecycle: Lifecycle) {
        loadValidators(lifecycle)
    }

    private suspend fun loadValidators(lifecycle: Lifecycle) = computationalCache.useCache(ELECTED_VALIDATORS_CACHE, lifecycle) {
        validatorProvider.getValidators(sharedState.chain(), ValidatorSource.Elected)
    }

    suspend fun create(lifecycle: Lifecycle): ValidatorRecommendator = withContext(Dispatchers.IO) {
        val validators: List<Validator> = loadValidators(lifecycle)

        ValidatorRecommendator(validators)
    }
}
