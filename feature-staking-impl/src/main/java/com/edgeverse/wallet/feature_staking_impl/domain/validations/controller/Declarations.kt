package com.edgeverse.wallet.feature_staking_impl.domain.validations.controller

import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_staking_impl.domain.validations.AccountIsNotControllerValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation

typealias SetControllerFeeValidation = EnoughToPayFeesValidation<SetControllerValidationPayload, SetControllerValidationFailure>
typealias IsNotControllerAccountValidation = AccountIsNotControllerValidation<SetControllerValidationPayload, SetControllerValidationFailure>
typealias SetControllerValidationSystem = ValidationSystem<SetControllerValidationPayload, SetControllerValidationFailure>