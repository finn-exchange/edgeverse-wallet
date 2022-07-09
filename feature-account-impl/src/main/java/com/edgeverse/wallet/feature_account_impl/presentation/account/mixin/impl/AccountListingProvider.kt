package com.edgeverse.wallet.feature_account_impl.presentation.account.mixin.impl

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.utils.IgnoredOnEquals
import com.edgeverse.wallet.common.utils.mapList
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_api.domain.model.LightMetaAccount
import com.edgeverse.wallet.feature_account_impl.presentation.account.mixin.api.AccountListingMixin
import com.edgeverse.wallet.feature_account_impl.presentation.account.model.LightMetaAccountUi

@Suppress("EXPERIMENTAL_API_USAGE")
class AccountListingProvider(
    private val accountInteractor: AccountInteractor,
    private val addressIconGenerator: AddressIconGenerator
) : AccountListingMixin {

    override fun accountsFlow() = accountInteractor.lightMetaAccountsFlow()
        .mapList { mapMetaAccountToUi(it, addressIconGenerator) }

    private suspend fun mapMetaAccountToUi(
        metaAccount: LightMetaAccount,
        iconGenerator: AddressIconGenerator,
    ) = with(metaAccount) {
        val icon = iconGenerator.createAddressIcon(metaAccount.substrateAccountId, AddressIconGenerator.SIZE_MEDIUM)

        LightMetaAccountUi(
            id = id,
            name = name,
            isSelected = isSelected,
            picture = IgnoredOnEquals(icon)
        )
    }
}
