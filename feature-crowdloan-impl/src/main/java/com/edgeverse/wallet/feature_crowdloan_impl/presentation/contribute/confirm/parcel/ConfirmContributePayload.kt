package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.confirm.parcel

import android.os.Parcelable
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ParachainMetadataParcelModel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class ConfirmContributePayload(
    val paraId: ParaId,
    val fee: BigDecimal,
    val amount: BigDecimal,
    val bonusPayload: BonusPayload?,
    val customizationPayload: Parcelable?,
    val metadata: ParachainMetadataParcelModel?,
    val estimatedRewardDisplay: String?,
) : Parcelable
