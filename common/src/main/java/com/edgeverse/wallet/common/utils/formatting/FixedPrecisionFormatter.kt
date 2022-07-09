package com.edgeverse.wallet.common.utils.formatting

import com.edgeverse.wallet.common.utils.decimalFormatterFor
import com.edgeverse.wallet.common.utils.patternWith
import java.math.BigDecimal

class FixedPrecisionFormatter(
    private val precision: Int
) : NumberFormatter {

    private val delegate = decimalFormatterFor(patternWith(precision))

    override fun format(number: BigDecimal): String {
        return delegate.format(number)
    }
}
