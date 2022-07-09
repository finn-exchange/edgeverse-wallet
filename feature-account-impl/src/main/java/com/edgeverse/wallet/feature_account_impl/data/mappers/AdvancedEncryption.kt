package com.edgeverse.wallet.feature_account_impl.data.mappers

import com.edgeverse.wallet.common.utils.input.valueOrNull
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryption
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInput
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator

fun mapAdvancedEncryptionStateToResponse(
    input: AdvancedEncryptionInput
): AdvancedEncryptionCommunicator.Response {
    return with(input) {
        AdvancedEncryptionCommunicator.Response(
            substrateCryptoType = substrateCryptoType.valueOrNull,
            substrateDerivationPath = substrateDerivationPath.valueOrNull,
            ethereumCryptoType = ethereumCryptoType.valueOrNull,
            ethereumDerivationPath = ethereumDerivationPath.valueOrNull
        )
    }
}

fun mapAdvancedEncryptionResponseToAdvancedEncryption(
    advancedEncryptionResponse: AdvancedEncryptionCommunicator.Response
): AdvancedEncryption {
    return with(advancedEncryptionResponse) {
        AdvancedEncryption(
            substrateCryptoType = substrateCryptoType,
            ethereumCryptoType = ethereumCryptoType,
            derivationPaths = AdvancedEncryption.DerivationPaths(
                substrate = substrateDerivationPath,
                ethereum = ethereumDerivationPath
            )
        )
    }
}
