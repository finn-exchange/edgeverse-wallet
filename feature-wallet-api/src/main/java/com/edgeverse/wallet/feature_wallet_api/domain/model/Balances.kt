package com.edgeverse.wallet.feature_wallet_api.domain.model

import com.edgeverse.wallet.common.list.GroupedList
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

class Balances(
    val assets: GroupedList<AssetGroup, Asset>,
    val totalBalanceFiat: BigDecimal,
    val lockedBalanceFiat: BigDecimal
)

class AssetGroup(
    val chain: Chain,
    val groupBalanceFiat: BigDecimal,
    val zeroBalance: Boolean
)
