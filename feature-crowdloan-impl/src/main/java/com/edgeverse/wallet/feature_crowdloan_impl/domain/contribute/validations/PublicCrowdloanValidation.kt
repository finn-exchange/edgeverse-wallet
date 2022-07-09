package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations

import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.validOrError
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.supportsPrivateCrowdloans

class PublicCrowdloanValidation(
    private val customContributeManager: CustomContributeManager,
) : ContributeValidation {

    override suspend fun validate(value: ContributeValidationPayload): ValidationStatus<ContributeValidationFailure> {
        val isPublic = value.crowdloan.fundInfo.verifier == null

        val flowType = value.crowdloan.parachainMetadata?.customFlow
        val supportsPrivate = flowType?.let(customContributeManager::supportsPrivateCrowdloans) ?: false

        return validOrError(isPublic || supportsPrivate) {
            ContributeValidationFailure.PrivateCrowdloanNotSupported
        }
    }
}
