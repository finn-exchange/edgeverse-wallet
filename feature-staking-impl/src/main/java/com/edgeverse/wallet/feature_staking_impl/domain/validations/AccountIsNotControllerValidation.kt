package com.edgeverse.wallet.feature_staking_impl.domain.validations

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState

class AccountIsNotControllerValidation<P, E>(
    private val stakingRepository: StakingRepository,
    private val controllerAddressProducer: (P) -> String,
    private val sharedState: StakingSharedState,
    private val errorProducer: (P) -> E,
) : Validation<P, E> {
    override suspend fun validate(value: P): ValidationStatus<E> {
        val controllerAddress = controllerAddressProducer(value)
        val ledger = stakingRepository.ledger(sharedState.chainId(), controllerAddress)

        return if (ledger == null) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.ERROR, errorProducer(value))
        }
    }
}
