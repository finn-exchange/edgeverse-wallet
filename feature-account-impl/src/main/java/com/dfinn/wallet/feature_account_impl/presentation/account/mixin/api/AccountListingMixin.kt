package com.dfinn.wallet.feature_account_impl.presentation.account.mixin.api

import com.dfinn.wallet.feature_account_impl.presentation.account.model.LightMetaAccountUi
import kotlinx.coroutines.flow.Flow

interface AccountListingMixin {

    fun accountsFlow(): Flow<List<LightMetaAccountUi>>
}
