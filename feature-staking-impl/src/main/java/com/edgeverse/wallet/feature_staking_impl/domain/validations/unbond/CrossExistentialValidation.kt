package com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond

import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.validOrWarning
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks

class CrossExistentialValidation(
    private val walletConstants: WalletConstants,
) : UnbondValidation {

    override suspend fun validate(value: UnbondValidationPayload): ValidationStatus<UnbondValidationFailure> {
        val tokenConfiguration = value.asset.token.configuration

        val existentialDepositInPlanks = walletConstants.existentialDeposit(tokenConfiguration.chainId)
        val existentialDeposit = tokenConfiguration.amountFromPlanks(existentialDepositInPlanks)

        val bonded = value.asset.bonded
        val resultGreaterThanExistential = bonded - value.amount >= existentialDeposit
        val resultIsZero = bonded == value.amount

        return validOrWarning(resultGreaterThanExistential || resultIsZero) {
            UnbondValidationFailure.BondedWillCrossExistential(willBeUnbonded = bonded)
        }
    }
}
