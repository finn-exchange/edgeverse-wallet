package com.dfinn.wallet.feature_staking_impl.domain.validations

import com.dfinn.wallet.common.validation.DefaultFailureLevel
import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationFailure
import com.dfinn.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationPayload
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.runtime.ext.accountIdOf
import com.dfinn.wallet.runtime.state.chain
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
