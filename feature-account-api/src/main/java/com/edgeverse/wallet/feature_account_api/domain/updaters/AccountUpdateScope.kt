package com.edgeverse.wallet.feature_account_api.domain.updaters

import com.edgeverse.wallet.core.updater.UpdateScope
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import kotlinx.coroutines.flow.Flow

class AccountUpdateScope(
    private val accountRepository: AccountRepository
) : UpdateScope {

    override fun invalidationFlow(): Flow<MetaAccount> {
        return accountRepository.selectedMetaAccountFlow()
    }

    suspend fun getAccount() = accountRepository.getSelectedMetaAccount()
}
