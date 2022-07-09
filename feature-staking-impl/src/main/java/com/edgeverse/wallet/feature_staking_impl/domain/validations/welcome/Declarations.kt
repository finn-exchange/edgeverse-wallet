package com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome

import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_staking_impl.domain.validations.MaxNominatorsReachedValidation

typealias WelcomeStakingValidationSystem = ValidationSystem<WelcomeStakingValidationPayload, WelcomeStakingValidationFailure>
typealias WelcomeStakingMaxNominatorsValidation = MaxNominatorsReachedValidation<WelcomeStakingValidationPayload, WelcomeStakingValidationFailure>