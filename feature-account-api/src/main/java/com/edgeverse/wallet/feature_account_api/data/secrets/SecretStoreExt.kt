package com.edgeverse.wallet.feature_account_api.data.secrets

import com.edgeverse.wallet.common.data.secrets.v2.*
import com.edgeverse.wallet.common.utils.fold
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_account_api.domain.model.accountIdIn
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair

suspend fun SecretStoreV2.getAccountSecrets(
    metaAccount: MetaAccount,
    chain: Chain
): AccountSecrets {
    val accountId = metaAccount.accountIdIn(chain) ?: error("No account for chain $chain in meta account ${metaAccount.name}")

    return getAccountSecrets(metaAccount.id, accountId)
}

fun AccountSecrets.keypair(chain: Chain): Keypair {
    return fold(
        left = { mapMetaAccountSecretsToKeypair(it, ethereum = chain.isEthereumBased) },
        right = { mapChainAccountSecretsToKeypair(it) }
    )
}

fun AccountSecrets.derivationPath(chain: Chain): String? {
    return fold(
        left = { mapMetaAccountSecretsToDerivationPath(it, ethereum = chain.isEthereumBased) },
        right = { it[ChainAccountSecrets.DerivationPath] }
    )
}
