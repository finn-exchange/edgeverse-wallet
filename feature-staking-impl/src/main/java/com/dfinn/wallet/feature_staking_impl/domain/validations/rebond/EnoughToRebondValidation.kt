package com.dfinn.wallet.feature_staking_impl.domain.validations.rebond

import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.common.validation.validOrError

class EnoughToRebondValidation : RebondValidation {

    override suspend fun validate(value: RebondValidationPayload): ValidationStatus<RebondValidationFailure> {
        return validOrError(value.rebondAmount <= value.controllerAsset.unbonding) {
            RebondValidationFailure.NOT_ENOUGH_UNBONDINGS
        }
    }
}
