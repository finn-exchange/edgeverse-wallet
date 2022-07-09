package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.base

import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.MinContributionValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.validations.AcalaMinContributionValidation
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.ContributeCustomization

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
