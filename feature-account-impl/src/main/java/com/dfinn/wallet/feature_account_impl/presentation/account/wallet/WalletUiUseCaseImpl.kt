package com.dfinn.wallet.feature_account_impl.presentation.account.wallet

import android.graphics.drawable.Drawable
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletModel
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class WalletUiUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val addressIconGenerator: AddressIconGenerator,
) : WalletUiUseCase {

    override fun selectedWalletUiFlow(
        showAddressIcon: Boolean
    ): Flow<WalletModel> {
        return accountRepository.selectedMetaAccountFlow().mapLatest { metaAccount ->
            val icon = maybeGenerateIcon(accountId = metaAccount.substrateAccountId, shouldGenerate = showAddressIcon)

            WalletModel(
                name = metaAccount.name,
                icon = icon
            )
        }
    }

    private suspend fun maybeGenerateIcon(accountId: AccountId, shouldGenerate: Boolean): Drawable? {
        return if (shouldGenerate) {
            addressIconGenerator.createAddressIcon(
                accountId = accountId,
                sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
                backgroundColorRes = AddressIconGenerator.BACKGROUND_TRANSPARENT
            )
        } else {
            null
        }
    }
}
