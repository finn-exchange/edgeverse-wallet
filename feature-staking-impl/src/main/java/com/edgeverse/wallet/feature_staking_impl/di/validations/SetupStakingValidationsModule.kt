package com.edgeverse.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.validation.CompositeValidation
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.MinimumAmountValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingFeeValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingMaximumNominatorsValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.assetBalanceProducer
import java.math.BigDecimal

@Module
class SetupStakingValidationsModule {

    @Provides
    @FeatureScope
    fun provideSetupStakingFeeValidation(
        stakingSharedState: StakingSharedState,
        walletRepository: WalletRepository,
    ): SetupStakingFeeValidation {
        return EnoughToPayFeesValidation(
            feeExtractor = { it.maxFee },
            availableBalanceProducer = SetupStakingFeeValidation.assetBalanceProducer(
                walletRepository,
                originAddressExtractor = { it.controllerAddress },
                chainAssetExtractor = { it.asset.token.configuration },
                stakingSharedState = stakingSharedState
            ),
            errorProducer = { SetupStakingValidationFailure.CannotPayFee },
            extraAmountExtractor = { it.bondAmount ?: BigDecimal.ZERO }
        )
    }

    @Provides
    @FeatureScope
    fun provideMinimumAmountValidation(
        stakingRepository: StakingRepository
    ) = MinimumAmountValidation(stakingRepository)

    @Provides
    @FeatureScope
    fun provideMaxNominatorsReachedValidation(
        stakingSharedState: StakingSharedState,
        stakingRepository: StakingRepository
    ) = SetupStakingMaximumNominatorsValidation(
        stakingRepository = stakingRepository,
        errorProducer = { SetupStakingValidationFailure.MaxNominatorsReached },
        isAlreadyNominating = SetupStakingPayload::isAlreadyNominating,
        sharedState = stakingSharedState
    )

    @Provides
    @FeatureScope
    fun provideSetupStakingValidationSystem(
        enoughToPayFeesValidation: SetupStakingFeeValidation,
        minimumAmountValidation: MinimumAmountValidation,
        maxNominatorsReachedValidation: SetupStakingMaximumNominatorsValidation
    ) = ValidationSystem(
        CompositeValidation(
            listOf(
                enoughToPayFeesValidation,
                minimumAmountValidation,
                maxNominatorsReachedValidation
            )
        )
    )
}
