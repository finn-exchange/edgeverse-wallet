package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam

import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation

typealias MoonbeamTermsValidationSystem = ValidationSystem<MoonbeamTermsPayload, MoonbeamTermsValidationFailure>
typealias MoonbeamTermsValidation = Validation<MoonbeamTermsPayload, MoonbeamTermsValidationFailure>
typealias MoonbeamTermsFeeValidation = EnoughToPayFeesValidation<MoonbeamTermsPayload, MoonbeamTermsValidationFailure>