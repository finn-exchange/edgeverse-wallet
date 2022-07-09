package com.edgeverse.wallet.feature_crowdloan_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.BonusAppliedValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.CapExceededValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeEnoughToPayFeesValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeExistentialDepositValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidationFailure
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.CrowdloanNotEndedValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.DefaultMinContributionValidation
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.PublicCrowdloanValidation
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.edgeverse.wallet.runtime.repository.ChainStateRepository

@Module
class ContributeValidationsModule {

    @Provides
    @FeatureScope
    fun provideFeesValidation() = ContributeEnoughToPayFeesValidation(
        feeExtractor = { it.fee },
        availableBalanceProducer = { it.asset.transferable },
        extraAmountExtractor = { it.contributionAmount },
        errorProducer = { ContributeValidationFailure.CannotPayFees }
    )

    @Provides
    @FeatureScope
    fun provideMinContributionValidation(
        crowdloanRepository: CrowdloanRepository,
    ) = DefaultMinContributionValidation(crowdloanRepository)

    @Provides
    @FeatureScope
    fun provideCapExceededValidation() = CapExceededValidation()

    @Provides
    @FeatureScope
    fun provideCrowdloanNotEndedValidation(
        chainStateRepository: ChainStateRepository,
        crowdloanRepository: CrowdloanRepository,
    ) = CrowdloanNotEndedValidation(chainStateRepository, crowdloanRepository)

    @Provides
    @FeatureScope
    fun provideExistentialWarningValidation(
        walletConstants: WalletConstants,
    ) = ContributeExistentialDepositValidation(
        totalBalanceProducer = { it.asset.total },
        feeProducer = { it.fee },
        extraAmountProducer = { it.contributionAmount },
        existentialDeposit = {
            val inPlanks = walletConstants.existentialDeposit(it.asset.token.configuration.chainId)

            it.asset.token.amountFromPlanks(inPlanks)
        },
        errorProducer = { ContributeValidationFailure.ExistentialDepositCrossed },
    )

    @Provides
    @FeatureScope
    fun providePublicCrowdloanValidation(
        customContributeManager: CustomContributeManager,
    ) = PublicCrowdloanValidation(customContributeManager)

    @Provides
    @FeatureScope
    fun provideBonusAppliedValidation(
        customContributeManager: CustomContributeManager,
    ) = BonusAppliedValidation(customContributeManager)

    @Provides
    @Select
    @FeatureScope
    fun provideSelectContributeValidationSet(
        feesValidation: ContributeEnoughToPayFeesValidation,
        minContributionValidation: DefaultMinContributionValidation,
        capExceededValidation: CapExceededValidation,
        crowdloanNotEndedValidation: CrowdloanNotEndedValidation,
        contributeExistentialDepositValidation: ContributeExistentialDepositValidation,
        publicCrowdloanValidation: PublicCrowdloanValidation,
        bonusAppliedValidation: BonusAppliedValidation,
    ): Set<ContributeValidation> = setOf(
        feesValidation,
        minContributionValidation,
        capExceededValidation,
        crowdloanNotEndedValidation,
        contributeExistentialDepositValidation,
        publicCrowdloanValidation,
        bonusAppliedValidation
    )

    @Provides
    @Confirm
    @FeatureScope
    fun provideConfirmContributeValidationSet(
        feesValidation: ContributeEnoughToPayFeesValidation,
        minContributionValidation: DefaultMinContributionValidation,
        capExceededValidation: CapExceededValidation,
        crowdloanNotEndedValidation: CrowdloanNotEndedValidation,
        contributeExistentialDepositValidation: ContributeExistentialDepositValidation,
        publicCrowdloanValidation: PublicCrowdloanValidation,
    ): Set<ContributeValidation> = setOf(
        feesValidation,
        minContributionValidation,
        capExceededValidation,
        crowdloanNotEndedValidation,
        contributeExistentialDepositValidation,
        publicCrowdloanValidation,
    )
}
