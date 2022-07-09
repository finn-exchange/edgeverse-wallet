package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks

class CapExceededValidation : ContributeValidation {

    override suspend fun validate(value: ContributeValidationPayload): ValidationStatus<ContributeValidationFailure> {
        val token = value.asset.token

        return with(value.crowdloan.fundInfo) {
            val raisedAmount = token.amountFromPlanks(raised)
            val capAmount = token.amountFromPlanks(cap)

            when {
                raisedAmount >= capAmount -> ValidationStatus.NotValid(DefaultFailureLevel.ERROR, ContributeValidationFailure.CapExceeded.FromRaised)
                raisedAmount + value.contributionAmount > capAmount -> {
                    val maxAllowedContribution = capAmount - raisedAmount

                    val reason = ContributeValidationFailure.CapExceeded.FromAmount(maxAllowedContribution, token.configuration)

                    ValidationStatus.NotValid(DefaultFailureLevel.ERROR, reason)
                }
                else -> ValidationStatus.Valid()
            }
        }
    }
}
