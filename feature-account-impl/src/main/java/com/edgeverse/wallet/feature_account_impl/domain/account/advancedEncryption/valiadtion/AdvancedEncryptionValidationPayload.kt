package com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion

import com.edgeverse.wallet.common.utils.input.Input

class AdvancedEncryptionValidationPayload(
    val substrateDerivationPathInput: Input<String>,
    val ethereumDerivationPathInput: Input<String>,
)
