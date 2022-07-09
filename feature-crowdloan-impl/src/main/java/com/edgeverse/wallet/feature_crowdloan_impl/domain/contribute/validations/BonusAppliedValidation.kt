package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager

class BonusAppliedValidation(
    private val customContributeManager: CustomContributeManager,
) : ContributeValidation {

    override suspend fun validate(value: ContributeValidationPayload): ValidationStatus<ContributeValidationFailure> {
        val factory = value.crowdloan.parachainMetadata?.customFlow?.let {
            customContributeManager.getFactoryOrNull(it)
        }

        val shouldHaveBonusPayload = factory?.extraBonusFlow != null

        return if (shouldHaveBonusPayload && value.bonusPayload == null) {
            ValidationStatus.NotValid(DefaultFailureLevel.WARNING, ContributeValidationFailure.BonusNotApplied)
        } else {
            ValidationStatus.Valid()
        }
    }
}
