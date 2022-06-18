package com.dfinn.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.validation.CompositeValidation
import com.dfinn.wallet.feature_staking_impl.domain.validations.reedeem.RedeemFeeValidation
import com.dfinn.wallet.feature_staking_impl.domain.validations.reedeem.RedeemValidationFailure
import com.dfinn.wallet.feature_staking_impl.domain.validations.reedeem.RedeemValidationSystem

@Module
class RedeemValidationsModule {

    @FeatureScope
    @Provides
    fun provideFeeValidation() = RedeemFeeValidation(
        feeExtractor = { it.fee },
        availableBalanceProducer = { it.asset.transferable },
        errorProducer = { RedeemValidationFailure.CANNOT_PAY_FEES }
    )

    @FeatureScope
    @Provides
    fun provideRedeemValidationSystem(
        feeValidation: RedeemFeeValidation,
    ) = RedeemValidationSystem(
        CompositeValidation(
            validations = listOf(
                feeValidation
            )
        )
    )
}
