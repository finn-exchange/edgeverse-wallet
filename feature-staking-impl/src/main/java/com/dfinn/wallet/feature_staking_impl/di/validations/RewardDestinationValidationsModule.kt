package com.dfinn.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.validation.CompositeValidation
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationControllerRequiredValidation
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationFeeValidation
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationFailure
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationSystem

@Module
class RewardDestinationValidationsModule {

    @FeatureScope
    @Provides
    fun provideFeeValidation() = RewardDestinationFeeValidation(
        feeExtractor = { it.fee },
        availableBalanceProducer = { it.availableControllerBalance },
        errorProducer = { RewardDestinationValidationFailure.CannotPayFees }
    )

    @Provides
    @FeatureScope
    fun controllerRequiredValidation(
        stakingSharedState: StakingSharedState,
        accountRepository: AccountRepository,
    ) = RewardDestinationControllerRequiredValidation(
        accountRepository = accountRepository,
        accountAddressExtractor = { it.stashState.controllerAddress },
        errorProducer = RewardDestinationValidationFailure::MissingController,
        sharedState = stakingSharedState
    )

    @FeatureScope
    @Provides
    fun provideRedeemValidationSystem(
        feeValidation: RewardDestinationFeeValidation,
        controllerRequiredValidation: RewardDestinationControllerRequiredValidation,
    ) = RewardDestinationValidationSystem(
        CompositeValidation(
            validations = listOf(
                feeValidation,
                controllerRequiredValidation
            )
        )
    )
}
