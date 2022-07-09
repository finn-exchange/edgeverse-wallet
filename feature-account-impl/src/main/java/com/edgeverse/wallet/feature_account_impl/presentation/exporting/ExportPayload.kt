package com.edgeverse.wallet.feature_account_impl.presentation.exporting

import android.os.Parcelable
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExportPayload(
    val metaId: Long,
    val chainId: ChainId
) : Parcelable
