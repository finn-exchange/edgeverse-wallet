package com.edgeverse.wallet.feature_staking_impl.domain.validations.payout

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus

class ProfitablePayoutValidation : Validation<MakePayoutPayload, PayoutValidationFailure> {

    override suspend fun validate(value: MakePayoutPayload): ValidationStatus<PayoutValidationFailure> {
        return if (value.fee < value.totalReward) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.WARNING, reason = PayoutValidationFailure.UnprofitablePayout)
        }
    }
}
