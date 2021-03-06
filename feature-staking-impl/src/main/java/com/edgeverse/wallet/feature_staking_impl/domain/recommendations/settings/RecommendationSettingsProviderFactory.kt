package com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings

import androidx.lifecycle.Lifecycle
import com.edgeverse.wallet.common.data.memory.ComputationalCache
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.repository.StakingConstantsRepository

private const val SETTINGS_PROVIDER_KEY = "SETTINGS_PROVIDER_KEY"

class RecommendationSettingsProviderFactory(
    private val computationalCache: ComputationalCache,
    private val stakingConstantsRepository: StakingConstantsRepository,
    private val sharedState: StakingSharedState,
) {

    suspend fun create(lifecycle: Lifecycle): RecommendationSettingsProvider {
        return computationalCache.useCache(SETTINGS_PROVIDER_KEY, lifecycle) {
            val chainId = sharedState.chainId()

            RecommendationSettingsProvider(
                maximumRewardedNominators = stakingConstantsRepository.maxRewardedNominatorPerValidator(chainId),
                maximumValidatorsPerNominator = stakingConstantsRepository.maxValidatorsPerNominator(chainId)
            )
        }
    }
}
