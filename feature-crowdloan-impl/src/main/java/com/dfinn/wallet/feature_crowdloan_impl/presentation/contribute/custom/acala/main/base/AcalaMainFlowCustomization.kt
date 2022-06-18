package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.base

import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidation
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.MinContributionValidation
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.validations.AcalaMinContributionValidation
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.ContributeCustomization

abstract class AcalaMainFlowCustomization<V> : ContributeCustomization<V> {

    override fun modifyValidations(validations: Collection<ContributeValidation>): Collection<ContributeValidation> {
        return validations.map {
            when (it) {
                is MinContributionValidation -> AcalaMinContributionValidation(fallback = it)
                else -> it
            }
        }
    }
}
