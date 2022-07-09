package com.edgeverse.wallet.feature_wallet_api.presentation.model

import androidx.annotation.StringRes
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.ensureSuffix
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

class ChooseAmountModel(
    val input: ChooseAmountInputModel,
    val balanceLabel: String?,
    val balance: String
)

class ChooseAmountInputModel(
    val tokenSymbol: String,
    val tokenIcon: String?,
)

internal fun ChooseAmountModel(
    asset: Asset,
    resourceManager: ResourceManager,
    retrieveAmount: (Asset) -> BigDecimal = Asset::transferable,
    @StringRes balanceLabelRes: Int?,
): ChooseAmountModel = ChooseAmountModel(
    input = ChooseAmountInputModel(asset.token.configuration),
    balanceLabel = balanceLabelRes?.let(resourceManager::getString)?.ensureSuffix(":"),
    balance = retrieveAmount(asset).formatTokenAmount(asset.token.configuration.symbol)
)

internal fun ChooseAmountInputModel(chainAsset: Chain.Asset): ChooseAmountInputModel = ChooseAmountInputModel(
    tokenSymbol = chainAsset.symbol,
    tokenIcon = chainAsset.iconUrl,
)
