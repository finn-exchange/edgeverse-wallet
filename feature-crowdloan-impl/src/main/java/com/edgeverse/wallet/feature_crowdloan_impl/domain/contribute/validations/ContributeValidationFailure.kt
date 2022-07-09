package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

sealed class ContributeValidationFailure {

    class LessThanMinContribution(
        val minContribution: BigDecimal,
        val chainAsset: Chain.Asset
    ) : ContributeValidationFailure()

    sealed class CapExceeded : ContributeValidationFailure() {

        class FromAmount(
            val maxAllowedContribution: BigDecimal,
            val chainAsset: Chain.Asset
        ) : CapExceeded()

        object FromRaised : CapExceeded()
    }

    object CrowdloanEnded : ContributeValidationFailure()

    object CannotPayFees : ContributeValidationFailure()

    object ExistentialDepositCrossed : ContributeValidationFailure()

    object BonusNotApplied : ContributeValidationFailure()

    object PrivateCrowdloanNotSupported : ContributeValidationFailure()
}
