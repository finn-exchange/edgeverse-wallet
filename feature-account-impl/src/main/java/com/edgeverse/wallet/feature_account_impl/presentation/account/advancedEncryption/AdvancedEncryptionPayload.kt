package com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption

import android.os.Parcelable
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class AdvancedEncryptionPayload : Parcelable {

    @Parcelize
    class Change(val addAccountPayload: AddAccountPayload) : AdvancedEncryptionPayload()

    @Parcelize
    class View(
        val metaAccountId: Long,
        val chainId: ChainId,
        val hideDerivationPaths: Boolean = false
    ) : AdvancedEncryptionPayload()
}
