package com.edgeverse.wallet.feature_wallet_api.presentation.formatters

import com.edgeverse.wallet.common.utils.format
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal
import java.math.BigInteger

fun BigInteger.formatPlanks(chainAsset: Chain.Asset): String {
    return chainAsset.amountFromPlanks(this).formatTokenAmount(chainAsset)
}

fun BigDecimal.formatTokenAmount(chainAsset: Chain.Asset): String {
    return formatTokenAmount(chainAsset.symbol)
}

fun BigDecimal.formatTokenAmount(tokenSymbol: String): String {
    return "${format()} $tokenSymbol"
}

fun BigDecimal.formatTokenChange(chainAsset: Chain.Asset, isIncome: Boolean): String {
    val withoutSign = formatTokenAmount(chainAsset)
    val sign = if (isIncome) '+' else '-'

    return sign + withoutSign
}
