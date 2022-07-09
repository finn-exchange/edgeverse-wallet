package com.edgeverse.wallet.feature_staking_impl.domain.validations

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.state.chain

class AccountRequiredValidation<P, E>(
    val accountRepository: AccountRepository,
    val accountAddressExtractor: (P) -> String,
    val sharedState: StakingSharedState,
    val errorProducer: (controllerAddress: String) -> E
) : Validation<P, E> {

    override suspend fun validate(value: P): ValidationStatus<E> {
        val accountAddress = accountAddressExtractor(value)
        val chain = sharedState.chain()

        return if (accountRepository.isAccountExists(chain.accountIdOf(accountAddress))) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.ERROR, errorProducer(accountAddress))
        }
    }
}
