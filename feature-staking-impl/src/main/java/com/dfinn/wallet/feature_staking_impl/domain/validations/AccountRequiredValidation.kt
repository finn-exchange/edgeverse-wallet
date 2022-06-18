package com.dfinn.wallet.feature_staking_impl.domain.validations

import com.dfinn.wallet.common.validation.DefaultFailureLevel
import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.runtime.ext.accountIdOf
import com.dfinn.wallet.runtime.state.chain

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
