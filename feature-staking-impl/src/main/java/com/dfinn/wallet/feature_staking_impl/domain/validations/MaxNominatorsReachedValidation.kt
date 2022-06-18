package com.dfinn.wallet.feature_staking_impl.domain.validations

import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.common.validation.validOrError
import com.dfinn.wallet.feature_staking_api.domain.api.StakingRepository
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState

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
