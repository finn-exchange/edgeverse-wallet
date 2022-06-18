package com.dfinn.wallet.feature_staking_impl.presentation.payouts.detail

import androidx.annotation.ColorRes
import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.feature_wallet_api.presentation.model.AmountModel

class PayoutDetailsModel(
    val validatorAddressModel: AddressModel,
    val timeLeft: Long,
    val timeLeftCalculatedAt: Long,
    @ColorRes val timerColor: Int,
    val eraDisplay: String,
    val reward: AmountModel,
)
