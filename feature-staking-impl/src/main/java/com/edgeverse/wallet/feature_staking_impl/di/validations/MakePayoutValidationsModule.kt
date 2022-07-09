package com.edgeverse.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.validation.CompositeValidation
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.PayoutFeeValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.PayoutValidationFailure
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.ProfitablePayoutValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingFeeValidation
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.assetBalanceProducer

@Module
class MakePayoutValidationsModule {

    @Provides
    @FeatureScope
    fun provideFeeValidation(
        stakingSharedState: StakingSharedState,
        walletRepository: WalletRepository,
    ): PayoutFeeValidation {
        return EnoughToPayFeesValidation(
            feeExtractor = { it.fee },
            availableBalanceProducer = SetupStakingFeeValidation.assetBalanceProducer(
                walletRepository,
                originAddressExtractor = { it.originAddress },
                chainAssetExtractor = { it.chainAsset },
                stakingSharedState = stakingSharedState
            ),
            errorProducer = { PayoutValidationFailure.CannotPayFee }
        )
    }

    @FeatureScope
    @Provides
    fun provideProfitableValidation() = ProfitablePayoutValidation()

    @Provides
    @FeatureScope
    fun provideValidationSystem(
        enoughToPayFeesValidation: PayoutFeeValidation,
        profitablePayoutValidation: ProfitablePayoutValidation,
    ) = ValidationSystem(
        CompositeValidation(
            listOf(
                enoughToPayFeesValidation,
                profitablePayoutValidation,
            )
        )
    )
}
