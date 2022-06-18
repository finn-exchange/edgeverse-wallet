package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.history

import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.balances.TransferExtrinsic
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.history.AssetHistory
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.TransactionFilter
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId

class UnsupportedAssetHistory : AssetHistory {

    override suspend fun fetchOperationsForBalanceChange(
        chain: Chain,
        blockHash: String,
        accountId: AccountId
    ): Result<List<TransferExtrinsic>> {
        return Result.failure(UnsupportedOperationException("Unsupported"))
    }

    override fun availableOperationFilters(asset: Chain.Asset): Set<TransactionFilter> {
        return emptySet()
    }
}
