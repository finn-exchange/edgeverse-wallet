package com.edgeverse.wallet.feature_assets.presentation

import android.os.Parcelable
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

@Parcelize
class AssetPayload(val chainId: ChainId, val chainAssetId: Int) : Parcelable
