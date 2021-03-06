package com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion

import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationSystem

typealias AdvancedEncryptionValidationSystem = ValidationSystem<AdvancedEncryptionValidationPayload, AdvancedEncryptionValidationFailure>
typealias AdvancedEncryptionValidation = Validation<AdvancedEncryptionValidationPayload, AdvancedEncryptionValidationFailure>
