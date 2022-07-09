package com.edgeverse.wallet.feature_staking_impl.domain.validations

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationFailure
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationPayload
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.state.chain
import java.math.BigDecimal

class NotZeroBalanceValidation(
    private val walletRepository: WalletRepository,
    private val stakingSharedState: StakingSharedState,
) : Validation<SetControllerValidationPayload, SetControllerValidationFailure> {

    override suspend fun validate(value: SetControllerValidationPayload): ValidationStatus<SetControllerValidationFailure> {
        val chain = stakingSharedState.chain()

        val controllerBalance = walletRepository.getAccountFreeBalance(chain.id, chain.accountIdOf(value.controllerAddress)).toBigDecimal()

        return if (controllerBalance > BigDecimal.ZERO) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.WARNING, SetControllerValidationFailure.ZERO_CONTROLLER_BALANCE)
        }
    }
}
