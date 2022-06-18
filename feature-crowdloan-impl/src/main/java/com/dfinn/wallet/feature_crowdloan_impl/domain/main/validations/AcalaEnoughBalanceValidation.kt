package com.dfinn.wallet.feature_crowdloan_impl.domain.main.validations

import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.acala.ContributionType
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidationPayload
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.MinContributionValidation
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.AcalaCustomizationPayload
import com.dfinn.wallet.feature_wallet_api.domain.model.planksFromAmount
import java.math.BigInteger

private val ACALA_MIN_CONTRIBUTION = 1.toBigDecimal()

class AcalaMinContributionValidation(
    private val fallback: MinContributionValidation,
) : MinContributionValidation() {

    override suspend fun minContribution(payload: ContributeValidationPayload): BigInteger {
        val customization = payload.customizationPayload
        require(customization is AcalaCustomizationPayload)

        return when (customization.contributionType) {
            ContributionType.DIRECT -> fallback.minContribution(payload)
            ContributionType.LIQUID -> {
                val asset = payload.asset.token.configuration

                return asset.planksFromAmount(ACALA_MIN_CONTRIBUTION)
            }
        }
    }
}
