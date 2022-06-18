package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain

import com.dfinn.wallet.common.data.network.runtime.binding.AccountInfo
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.runtime.AccountId

interface SubstrateRemoteSource {

    suspend fun getAccountInfo(
        chainId: ChainId,
        accountId: AccountId
    ): AccountInfo
}
