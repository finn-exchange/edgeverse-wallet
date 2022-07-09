package com.edgeverse.wallet.feature_onboarding_impl

import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload

interface OnboardingRouter {

    fun openCreateAccount(addAccountPayload: AddAccountPayload.MetaAccount)

    fun openMnemonicScreen(accountName: String?, payload: AddAccountPayload)

    fun openImportAccountScreen(payload: ImportAccountPayload)

    fun back()
}
