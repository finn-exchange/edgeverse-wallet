package com.edgeverse.wallet.feature_account_api.data.secrets

import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.data.secrets.v2.getChainAccountKeypair
import com.edgeverse.wallet.common.data.secrets.v2.getMetaAccountKeypair
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_account_api.domain.model.accountIdIn
import com.edgeverse.wallet.feature_account_api.domain.model.multiChainEncryptionFor
import com.edgeverse.wallet.feature_account_api.domain.model.multiChainEncryptionIn
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.encrypt.SignatureWrapper
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Sign

suspend fun SecretStoreV2.sign(
    metaAccount: MetaAccount,
    chain: Chain,
    message: String,
) = sign(metaAccount, chain, message.encodeToByteArray()).toHexString(withPrefix = true)

suspend fun SecretStoreV2.sign(
    metaAccount: MetaAccount,
    chain: Chain,
    message: ByteArray,
) = Signer.sign(
    multiChainEncryption = metaAccount.multiChainEncryptionIn(chain),
    message = message,
    keypair = getKeypair(metaAccount, chain),
).signature

suspend fun SecretStoreV2.signSubstrate(
    metaAccount: MetaAccount,
    accountId: AccountId,
    message: ByteArray
) = Signer.sign(
    multiChainEncryption = metaAccount.multiChainEncryptionFor(accountId),
    message = message,
    keypair = getSubstrateKeypair(metaAccount, accountId)
).signature

suspend fun SecretStoreV2.signEthereum(
    metaAccount: MetaAccount,
    accountId: AccountId,
    message: ByteArray,
): ByteArray {
    return signEthereum(metaAccount, accountId) {
        Sign.signMessage(message, it, false)
    }
}

suspend fun SecretStoreV2.signEthereumPrefixed(
    metaAccount: MetaAccount,
    accountId: AccountId,
    message: ByteArray
): ByteArray {
    return signEthereum(metaAccount, accountId) {
        Sign.signPrefixedMessage(message, it)
    }
}

private suspend inline fun SecretStoreV2.signEthereum(
    metaAccount: MetaAccount,
    accountId: AccountId,
    sign: (ECKeyPair) -> Sign.SignatureData
): ByteArray {
    val keypair = getEthereumKeypair(metaAccount, accountId)

    val signingData = sign(ECKeyPair.create(keypair.privateKey))

    return SignatureWrapper.Ecdsa(v = signingData.v, r = signingData.r, s = signingData.s).signature
}

/**
 * @return secrets for the given [accountId] in [metaAccount] respecting configuration of [chain] (is ethereum or not).
 */
suspend fun SecretStoreV2.getKeypair(
    metaAccount: MetaAccount,
    chain: Chain,
    accountId: AccountId
): Keypair {
    return if (hasChainSecrets(metaAccount.id, accountId)) {
        getChainAccountKeypair(metaAccount.id, accountId)
    } else {
        getMetaAccountKeypair(metaAccount.id, chain.isEthereumBased)
    }
}

/**
 * @return chain account secrets if there is a chain account in  [chain]. Meta account keypair otherwise.
 */
suspend fun SecretStoreV2.getKeypair(
    metaAccount: MetaAccount,
    chain: Chain,
): Keypair {
    val accountId = requireNotNull(metaAccount.accountIdIn(chain)) {
        "No account in ${metaAccount.name} for ${chain.name}"
    }

    return getKeypair(metaAccount, chain, accountId)
}

suspend fun SecretStoreV2.getSubstrateKeypair(
    metaAccount: MetaAccount,
    accountId: AccountId
): Keypair {
    return if (hasChainSecrets(metaAccount.id, accountId)) {
        getChainAccountKeypair(metaAccount.id, accountId)
    } else {
        getMetaAccountKeypair(metaAccount.id, isEthereum = false)
    }
}

suspend fun SecretStoreV2.getEthereumKeypair(
    metaAccount: MetaAccount,
    accountId: AccountId
): Keypair {
    return if (hasChainSecrets(metaAccount.id, accountId)) {
        getChainAccountKeypair(metaAccount.id, accountId)
    } else {
        getMetaAccountKeypair(metaAccount.id, isEthereum = true)
    }
}
