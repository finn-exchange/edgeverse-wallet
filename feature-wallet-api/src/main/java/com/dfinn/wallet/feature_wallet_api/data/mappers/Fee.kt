package com.dfinn.wallet.feature_wallet_api.data.mappers

import com.dfinn.wallet.feature_wallet_api.domain.model.Token
import com.dfinn.wallet.feature_wallet_api.presentation.model.FeeModel
import com.dfinn.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import java.math.BigDecimal

fun mapFeeToFeeModel(
    fee: BigDecimal,
    token: Token,
    includeZeroFiat: Boolean = true
) = FeeModel(
    fee = fee,
    display = mapAmountToAmountModel(fee, token, includeZeroFiat)
)
