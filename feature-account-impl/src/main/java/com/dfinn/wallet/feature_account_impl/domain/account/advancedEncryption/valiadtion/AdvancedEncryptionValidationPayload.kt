package com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion

import com.dfinn.wallet.common.utils.input.Input

class AdvancedEncryptionValidationPayload(
    val substrateDerivationPathInput: Input<String>,
    val ethereumDerivationPathInput: Input<String>,
)
