package com.edgeverse.wallet.feature_account_impl.domain.account.export.mnemonic

import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.data.secrets.v2.entropy
import com.edgeverse.wallet.feature_account_api.data.secrets.getAccountSecrets
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.MnemonicCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExportMnemonicInteractor(
    private val accountRepository: AccountRepository,
    private val secretStoreV2: SecretStoreV2,
    private val chainRegistry: ChainRegistry,
) {

    suspend fun getMnemonic(
        metaId: Long,
        chainId: ChainId,
    ): Mnemonic = withContext(Dispatchers.Default) {
        val metaAccount = accountRepository.getMetaAccount(metaId)
        val chain = chainRegistry.getChain(chainId)

        val entropy = secretStoreV2.getAccountSecrets(metaAccount, chain).entropy()
            ?: error("No mnemonic found for account ${metaAccount.name} in ${chain.name}")

        MnemonicCreator.fromEntropy(entropy)
    }
}
