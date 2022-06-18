package com.dfinn.wallet.feature_account_impl.domain.account.export.json

import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId

class ExportJsonInteractor(
    private val accountRepository: AccountRepository,
    private val chainRegistry: ChainRegistry,
) {

    suspend fun generateRestoreJson(
        metaId: Long,
        chainId: ChainId,
        password: String,
    ): Result<String> {
        val metaAccount = accountRepository.getMetaAccount(metaId)
        val chain = chainRegistry.getChain(chainId)

        return runCatching {
            accountRepository.generateRestoreJson(metaAccount, chain, password)
        }
    }
}
