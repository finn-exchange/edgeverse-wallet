package com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond

import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_staking_impl.domain.validations.UnbondingRequestsLimitValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.PositiveAmountValidation

typealias UnbondFeeValidation = EnoughToPayFeesValidation<UnbondValidationPayload, UnbondValidationFailure>
typealias NotZeroUnbondValidation = PositiveAmountValidation<UnbondValidationPayload, UnbondValidationFailure>
typealias UnbondLimitValidation = UnbondingRequestsLimitValidation<UnbondValidationPayload, UnbondValidationFailure>
typealias UnbondValidation = Validation<UnbondValidationPayload, UnbondValidationFailure>
typealias UnbondValidationSystem = ValidationSystem<UnbondValidationPayload, UnbondValidationFailure>
