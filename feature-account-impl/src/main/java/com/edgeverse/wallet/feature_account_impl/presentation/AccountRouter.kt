package com.edgeverse.wallet.feature_account_impl.presentation

import com.edgeverse.wallet.common.navigation.DelayedNavigation
import com.edgeverse.wallet.common.navigation.PinRequired
import com.edgeverse.wallet.common.navigation.SecureRouter
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.ExportPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicPayload

interface AccountRouter : SecureRouter {

    fun openMain()

    fun openCreatePincode()

    fun openMnemonicScreen(accountName: String?, payload: AddAccountPayload)

    fun openConfirmMnemonicOnCreate(confirmMnemonicPayload: ConfirmMnemonicPayload)

    fun back()

    fun openAccounts(accountChosenNavDirection: AccountChosenNavDirection)

    fun openNodes()

    fun openLanguages()

    fun openAddAccount(payload: AddAccountPayload)

    fun openAccountDetails(metaAccountId: Long)

    fun openEditAccounts()

    fun backToMainScreen()

    fun openNodeDetails(nodeId: Int)

    fun openAddNode()

    @PinRequired
    fun exportMnemonicAction(exportPayload: ExportPayload): DelayedNavigation

    @PinRequired
    fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation

    @PinRequired
    fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation

    fun openExportJsonConfirm(payload: ExportJsonConfirmPayload)

    fun openImportAccountScreen(payload: ImportAccountPayload)

    fun returnToWallet()

    fun finishExportFlow()

    fun openChangePinCode()
}
