package com.edgeverse.wallet.feature_staking_impl.domain.validations

import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.validOrError
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState

class MaxNominatorsReachedValidation<P, E>(
    private val stakingRepository: StakingRepository,
    private val isAlreadyNominating: (P) -> Boolean,
    private val sharedState: StakingSharedState,
    private val errorProducer: () -> E
) : Validation<P, E> {

    override suspend fun validate(value: P): ValidationStatus<E> {
        val chainId = sharedState.chainId()

        val nominatorCount = stakingRepository.nominatorsCount(chainId) ?: return ValidationStatus.Valid()
        val maxNominatorsAllowed = stakingRepository.maxNominators(chainId) ?: return ValidationStatus.Valid()

        if (isAlreadyNominating(value)) {
            return ValidationStatus.Valid()
        }

        return validOrError(nominatorCount < maxNominatorsAllowed) {
            errorProducer()
        }
    }
}
