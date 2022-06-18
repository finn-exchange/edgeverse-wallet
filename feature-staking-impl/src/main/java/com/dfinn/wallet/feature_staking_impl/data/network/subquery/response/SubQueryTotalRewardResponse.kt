package com.dfinn.wallet.feature_staking_impl.data.network.subquery.response

import com.dfinn.wallet.common.data.network.subquery.SubQueryNodes
import java.math.BigInteger

class SubQueryTotalRewardResponse(val accumulatedRewards: SubQueryNodes<TotalRewardNode>) {

    class TotalRewardNode(val amount: BigInteger)
}

val SubQueryTotalRewardResponse.totalReward: BigInteger
    get() = accumulatedRewards.nodes.firstOrNull()?.amount ?: BigInteger.ZERO
