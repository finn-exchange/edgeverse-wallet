package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.detail

import androidx.annotation.ColorRes
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountModel

class PayoutDetailsModel(
    val validatorAddressModel: AddressModel,
    val timeLeft: Long,
    val timeLeftCalculatedAt: Long,
    @ColorRes val timerColor: Int,
    val eraDisplay: String,
    val reward: AmountModel,
)
