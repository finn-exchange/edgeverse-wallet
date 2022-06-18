package com.dfinn.wallet.feature_assets.presentation

import android.os.Parcelable
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

@Parcelize
class AssetPayload(val chainId: ChainId, val chainAssetId: Int) : Parcelable
