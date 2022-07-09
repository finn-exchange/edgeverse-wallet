package com.edgeverse.wallet.feature_staking_impl.domain.validations.setup

import com.edgeverse.wallet.feature_staking_impl.domain.validations.MaxNominatorsReachedValidation
import com.edgeverse.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation

typealias SetupStakingFeeValidation = EnoughToPayFeesValidation<SetupStakingPayload, SetupStakingValidationFailure>
typealias SetupStakingMaximumNominatorsValidation = MaxNominatorsReachedValidation<SetupStakingPayload, SetupStakingValidationFailure>