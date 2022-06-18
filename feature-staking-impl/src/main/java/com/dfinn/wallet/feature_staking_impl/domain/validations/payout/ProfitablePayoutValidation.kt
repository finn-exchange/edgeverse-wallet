package com.dfinn.wallet.feature_staking_impl.domain.validations.payout

import com.dfinn.wallet.common.validation.DefaultFailureLevel
import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationStatus

class ProfitablePayoutValidation : Validation<MakePayoutPayload, PayoutValidationFailure> {

    override suspend fun validate(value: MakePayoutPayload): ValidationStatus<PayoutValidationFailure> {
        return if (value.fee < value.totalReward) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.WARNING, reason = PayoutValidationFailure.UnprofitablePayout)
        }
    }
}
