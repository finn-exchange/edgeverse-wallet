package com.edgeverse.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.validation.CompositeValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.EnoughToRebondValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.NotZeroRebondValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondFeeValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondValidationFailure
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondValidationSystem

@Module
class RebondValidationsModule {

    @FeatureScope
    @Provides
    fun provideFeeValidation() = RebondFeeValidation(
        feeExtractor = { it.fee },
        availableBalanceProducer = { it.controllerAsset.transferable },
        errorProducer = { RebondValidationFailure.CANNOT_PAY_FEE }
    )

    @FeatureScope
    @Provides
    fun provideNotZeroUnbondValidation() = NotZeroRebondValidation(
        amountExtractor = { it.rebondAmount },
        errorProvider = { RebondValidationFailure.ZERO_AMOUNT }
    )

    @FeatureScope
    @Provides
    fun provideEnoughToRebondValidation() = EnoughToRebondValidation()

    @FeatureScope
    @Provides
    fun provideRebondValidationSystem(
        rebondFeeValidation: RebondFeeValidation,
        notZeroRebondValidation: NotZeroRebondValidation,
        enoughToRebondValidation: EnoughToRebondValidation,
    ) = RebondValidationSystem(
        CompositeValidation(
            validations = listOf(
                rebondFeeValidation,
                notZeroRebondValidation,
                enoughToRebondValidation
            )
        )
    )
}
