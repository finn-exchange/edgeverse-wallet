package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_crowdloan_impl.domain.common.leaseIndexFromBlock
import com.edgeverse.wallet.runtime.repository.ChainStateRepository

class CrowdloanNotEndedValidation(
    private val chainStateRepository: ChainStateRepository,
    private val crowdloanRepository: CrowdloanRepository
) : ContributeValidation {

    override suspend fun validate(value: ContributeValidationPayload): ValidationStatus<ContributeValidationFailure> {
        val chainId = value.asset.token.configuration.chainId
        val currentBlock = chainStateRepository.currentBlock(chainId)

        val blocksPerLease = crowdloanRepository.blocksPerLeasePeriod(chainId)

        val currentLeaseIndex = leaseIndexFromBlock(currentBlock, blocksPerLease)

        return when {
            currentBlock >= value.crowdloan.fundInfo.end -> crowdloanEndedFailure()
            currentLeaseIndex > value.crowdloan.fundInfo.firstSlot -> crowdloanEndedFailure()
            else -> ValidationStatus.Valid()
        }
    }

    private fun crowdloanEndedFailure(): ValidationStatus.NotValid<ContributeValidationFailure> =
        ValidationStatus.NotValid(DefaultFailureLevel.ERROR, ContributeValidationFailure.CrowdloanEnded)
}