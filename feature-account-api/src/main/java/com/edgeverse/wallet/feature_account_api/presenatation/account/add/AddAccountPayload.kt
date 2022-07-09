package com.edgeverse.wallet.feature_account_api.presenatation.account.add

import android.os.Parcelable
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class AddAccountPayload : Parcelable {

    @Parcelize
    object MetaAccount : AddAccountPayload()

    @Parcelize
    class ChainAccount(val chainId: ChainId, val metaId: Long) : AddAccountPayload()
}

val AddAccountPayload.chainIdOrNull
    get() = (this as? AddAccountPayload.ChainAccount)?.chainId
