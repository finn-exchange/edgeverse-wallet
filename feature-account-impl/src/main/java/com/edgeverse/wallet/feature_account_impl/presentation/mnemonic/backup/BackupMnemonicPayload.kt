package com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.backup

import android.os.Parcelable
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class BackupMnemonicPayload : Parcelable {

    @Parcelize
    class Create(
        val newWalletName: String?,
        val addAccountPayload: AddAccountPayload
    ) : BackupMnemonicPayload()

    @Parcelize
    class Confirm(
        val chainId: ChainId,
        val metaAccountId: Long
    ) : BackupMnemonicPayload()
}
