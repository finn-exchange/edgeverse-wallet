package com.dfinn.wallet.feature_crowdloan_impl.di.validations

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.validation.CompositeValidation
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsFeeValidation
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsValidation
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsValidationFailure
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsValidationSystem

@Module
class MoonbeamTermsValidationsModule {

    @Provides
    @IntoSet
    @FeatureScope
    fun provideFeesValidation(): MoonbeamTermsValidation = MoonbeamTermsFeeValidation(
        feeExtractor = { it.fee },
        availableBalanceProducer = { it.asset.transferable },
        errorProducer = { MoonbeamTermsValidationFailure.CANNOT_PAY_FEES }
    )

    @Provides
    @FeatureScope
    fun provideValidationSystem(
        contributeValidations: @JvmSuppressWildcards Set<MoonbeamTermsValidation>
    ) = MoonbeamTermsValidationSystem(
        validation = CompositeValidation(contributeValidations.toList())
    )
}
