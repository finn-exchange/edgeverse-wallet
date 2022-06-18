package com.dfinn.wallet.feature_account_impl.presentation.exporting

import android.os.Parcelable
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExportPayload(
    val metaId: Long,
    val chainId: ChainId
) : Parcelable
