package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel

import android.os.Parcelable
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContributePayload(
    val paraId: ParaId,
    val parachainMetadata: ParachainMetadataParcelModel?
) : Parcelable
