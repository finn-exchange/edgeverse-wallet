package com.dfinn.wallet.feature_staking_impl.domain.validations.unbond

import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.common.validation.validOrError

class EnoughToUnbondValidation : UnbondValidation {

    override suspend fun validate(value: UnbondValidationPayload): ValidationStatus<UnbondValidationFailure> {
        return validOrError(value.amount <= value.asset.bonded) {
            UnbondValidationFailure.NotEnoughBonded
        }
    }
}
