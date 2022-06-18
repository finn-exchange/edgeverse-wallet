@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain

import com.dfinn.wallet.common.data.network.runtime.binding.AccountInfo
import com.dfinn.wallet.common.data.network.runtime.binding.bindAccountInfo
import com.dfinn.wallet.common.utils.system
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import com.dfinn.wallet.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class WssSubstrateSource(
    private val remoteStorageSource: StorageDataSource,
) : SubstrateRemoteSource {

    override suspend fun getAccountInfo(
        chainId: ChainId,
        accountId: AccountId,
    ): AccountInfo {
        return remoteStorageSource.query(
            chainId = chainId,
            keyBuilder = {
                it.metadata.system().storage("Account").storageKey(it, accountId)
            },
            binding = { scale, runtime ->
                scale?.let { bindAccountInfo(it, runtime) } ?: AccountInfo.empty()
            }
        )
    }
}
