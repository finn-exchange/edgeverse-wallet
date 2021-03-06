package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model

import android.os.Parcelable
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
class ConfirmPayoutPayload(
    val payouts: List<PendingPayoutParcelable>,
    val totalRewardInPlanks: BigInteger
) : Parcelable
