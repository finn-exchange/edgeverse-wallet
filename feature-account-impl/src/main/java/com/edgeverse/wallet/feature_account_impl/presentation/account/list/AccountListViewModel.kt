package com.edgeverse.wallet.feature_account_impl.presentation.account.list

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.account.mixin.api.AccountListingMixin
import com.edgeverse.wallet.feature_account_impl.presentation.account.model.LightMetaAccountUi
import kotlinx.coroutines.launch

enum class AccountChosenNavDirection {
    BACK, MAIN
}

class AccountListViewModel(
    private val accountInteractor: AccountInteractor,
    private val accountRouter: AccountRouter,
    private val accountChosenNavDirection: AccountChosenNavDirection,
    accountListingMixin: AccountListingMixin,
) : BaseViewModel() {

    val accountsFlow = accountListingMixin.accountsFlow()
        .inBackground()
        .share()

    fun infoClicked(accountModel: LightMetaAccountUi) {
        accountRouter.openAccountDetails(accountModel.id)
    }

    fun editClicked() {
        accountRouter.openEditAccounts()
    }

    fun selectAccountClicked(account: LightMetaAccountUi) = launch {
        accountInteractor.selectMetaAccount(account.id)

        dispatchNavigation()
    }

    private fun dispatchNavigation() {
        when (accountChosenNavDirection) {
            AccountChosenNavDirection.BACK -> accountRouter.back()
            AccountChosenNavDirection.MAIN -> accountRouter.returnToWallet()
        }
    }

    fun backClicked() {
        accountRouter.back()
    }

    fun addAccountClicked() {
        accountRouter.openAddAccount(AddAccountPayload.MetaAccount)
    }
}
