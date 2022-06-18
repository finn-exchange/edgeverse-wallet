package com.dfinn.wallet.common.utils.formatting

import java.math.BigDecimal

class NumberAbbreviation(
    val threshold: BigDecimal,
    val divisor: BigDecimal,
    val suffix: String,
    val formatter: NumberFormatter,
)
