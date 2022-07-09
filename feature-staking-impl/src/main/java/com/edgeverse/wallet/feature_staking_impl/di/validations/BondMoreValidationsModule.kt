package com.edgeverse.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.validation.CompositeValidation
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreFeeValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationFailure
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationSystem
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.NotZeroBondValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingFeeValidation
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.assetBalanceProducer

@Module
class BondMoreValidationsModule {

    @Provides
    @FeatureScope
    fun provideFeeValidation(
        stakingSharedState: StakingSharedState,
        walletRepository: WalletRepository,
    ): BondMoreFeeValidation {
        return EnoughToPayFeesValidation(
            feeExtractor = { it.fee },
            availableBalanceProducer = SetupStakingFeeValidation.assetBalanceProducer(
                walletRepository,
                originAddressExtractor = { it.stashAddress },
                chainAssetExtractor = { it.chainAsset },
                stakingSharedState = stakingSharedState
            ),
            errorProducer = { BondMoreValidationFailure.NOT_ENOUGH_TO_PAY_FEES },
            extraAmountExtractor = { it.amount }
        )
    }

    @Provides
    @FeatureScope
    fun provideNotZeroBondValidation() = NotZeroBondValidation(
        amountExtractor = BondMoreValidationPayload::amount,
        errorProvider = { BondMoreValidationFailure.ZERO_BOND }
    )

    @Provides
    @FeatureScope
    fun provideBondMoreValidationSystem(
        bondMoreFeeValidation: BondMoreFeeValidation,
        notZeroBondValidation: NotZeroBondValidation,
    ) = BondMoreValidationSystem(
        validation = CompositeValidation(
            validations = listOf(
                bondMoreFeeValidation,
                notZeroBondValidation
            )
        )
    )
}
