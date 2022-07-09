package com.edgeverse.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.validation.CompositeValidation
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.NotZeroBalanceValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.IsNotControllerAccountValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerFeeValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationFailure
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationSystem
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation

@Module
class SetControllerValidationsModule {

    @FeatureScope
    @Provides
    fun provideFeeValidation(): SetControllerFeeValidation {
        return EnoughToPayFeesValidation(
            feeExtractor = { it.fee },
            availableBalanceProducer = { it.transferable },
            errorProducer = { SetControllerValidationFailure.NOT_ENOUGH_TO_PAY_FEES }
        )
    }

    @FeatureScope
    @Provides
    fun provideControllerValidation(
        stakingSharedState: StakingSharedState,
        stakingRepository: StakingRepository
    ) = IsNotControllerAccountValidation(
        stakingRepository = stakingRepository,
        controllerAddressProducer = { it.controllerAddress },
        errorProducer = { SetControllerValidationFailure.ALREADY_CONTROLLER },
        sharedState = stakingSharedState
    )

    @FeatureScope
    @Provides
    fun provideZeroBalanceControllerValidation(
        stakingSharedState: StakingSharedState,
        walletRepository: WalletRepository
    ): NotZeroBalanceValidation {
        return NotZeroBalanceValidation(
            walletRepository = walletRepository,
            stakingSharedState = stakingSharedState
        )
    }

    @FeatureScope
    @Provides
    fun provideSetControllerValidationSystem(
        enoughToPayFeesValidation: SetControllerFeeValidation,
        isNotControllerAccountValidation: IsNotControllerAccountValidation,
        controllerAccountIsNotZeroBalance: NotZeroBalanceValidation
    ) = SetControllerValidationSystem(
        CompositeValidation(
            validations = listOf(
                enoughToPayFeesValidation,
                isNotControllerAccountValidation,
                controllerAccountIsNotZeroBalance
            )
        )
    )
}
