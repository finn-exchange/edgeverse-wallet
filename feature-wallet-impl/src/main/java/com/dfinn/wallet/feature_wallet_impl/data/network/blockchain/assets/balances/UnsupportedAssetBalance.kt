package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.balances

import com.dfinn.wallet.common.data.network.runtime.binding.BlockHash
import com.dfinn.wallet.core.updater.SubscriptionBuilder
import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.balances.AssetBalance
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.math.BigInteger

class UnsupportedAssetBalance : AssetBalance {

    override suspend fun existentialDeposit(chain: Chain, chainAsset: Chain.Asset): BigInteger {
        throw UnsupportedOperationException("UnsupportedBalanceSource")
    }

    override suspend fun queryTotalBalance(chain: Chain, chainAsset: Chain.Asset, accountId: AccountId): BigInteger {
        throw UnsupportedOperationException("UnsupportedBalanceSource")
    }

    override suspend fun startSyncingBalance(
        chain: Chain,
        chainAsset: Chain.Asset,
        metaAccount: MetaAccount,
        accountId: AccountId,
        subscriptionBuilder: SubscriptionBuilder
    ): Flow<BlockHash> {
        return emptyFlow()
    }
}
