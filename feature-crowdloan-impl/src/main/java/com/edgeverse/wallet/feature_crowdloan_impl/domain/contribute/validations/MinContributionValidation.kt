package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations

import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.validOrError
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import java.math.BigInteger

class DefaultMinContributionValidation(
    private val crowdloanRepository: CrowdloanRepository,
) : MinContributionValidation() {

    override suspend fun minContribution(payload: ContributeValidationPayload): BigInteger {
        val chainAsset = payload.asset.token.configuration

        return crowdloanRepository.minContribution(chainAsset.chainId)
    }
}

abstract class MinContributionValidation : ContributeValidation {

    abstract suspend fun minContribution(payload: ContributeValidationPayload): BigInteger

    override suspend fun validate(value: ContributeValidationPayload): ValidationStatus<ContributeValidationFailure> {
        val chainAsset = value.asset.token.configuration

        val minContributionInPlanks = minContribution(value)
        val minContribution = chainAsset.amountFromPlanks(minContributionInPlanks)

        return validOrError(value.contributionAmount >= minContribution) {
            ContributeValidationFailure.LessThanMinContribution(minContribution, chainAsset)
        }
    }
}
