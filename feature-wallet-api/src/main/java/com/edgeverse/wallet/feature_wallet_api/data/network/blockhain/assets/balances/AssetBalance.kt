package com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.balances

import com.edgeverse.wallet.common.data.network.runtime.binding.BlockHash
import com.edgeverse.wallet.core.updater.SubscriptionBuilder
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

interface AssetBalance {

    suspend fun existentialDeposit(
        chain: Chain,
        chainAsset: Chain.Asset
    ): BigInteger

    suspend fun queryTotalBalance(
        chain: Chain,
        chainAsset: Chain.Asset,
        accountId: AccountId
    ): BigInteger

    /**
     * @return emits hash of the blocks where changes occurred. If no change were detected based on the upstream event - should emit null
     */
    suspend fun startSyncingBalance(
        chain: Chain,
        chainAsset: Chain.Asset,
        metaAccount: MetaAccount,
        accountId: AccountId,
        subscriptionBuilder: SubscriptionBuilder
    ): Flow<BlockHash?>
}
