package com.dfinn.wallet.feature_staking_impl.domain.validations.setup

import com.dfinn.wallet.common.validation.DefaultFailureLevel
import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.feature_staking_api.domain.api.StakingRepository
import com.dfinn.wallet.feature_wallet_api.domain.model.amountFromPlanks

class MinimumAmountValidation(
    private val stakingRepository: StakingRepository,
) : Validation<SetupStakingPayload, SetupStakingValidationFailure> {

    override suspend fun validate(value: SetupStakingPayload): ValidationStatus<SetupStakingValidationFailure> {
        val assetConfiguration = value.asset.token.configuration

        val minimumBondInPlanks = stakingRepository.minimumNominatorBond(assetConfiguration.chainId)
        val minimumBond = assetConfiguration.amountFromPlanks(minimumBondInPlanks)

        // either first time bond or already existing bonded balance
        val amountToCheckAgainstMinimum = value.bondAmount ?: value.asset.bonded

        return if (amountToCheckAgainstMinimum >= minimumBond) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.ERROR, SetupStakingValidationFailure.TooSmallAmount(minimumBond))
        }
    }
}
