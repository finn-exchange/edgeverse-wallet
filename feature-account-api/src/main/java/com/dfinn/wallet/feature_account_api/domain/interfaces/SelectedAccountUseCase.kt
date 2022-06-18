package com.dfinn.wallet.feature_account_api.domain.interfaces

import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import kotlinx.coroutines.flow.Flow

class SelectedAccountUseCase(
    private val accountRepository: AccountRepository
) {

    fun selectedMetaAccountFlow(): Flow<MetaAccount> = accountRepository.selectedMetaAccountFlow()

    suspend fun getSelectedMetaAccount(): MetaAccount = accountRepository.getSelectedMetaAccount()
}
