package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.dfinn.wallet.common.data.network.rpc.BulkRetriever
import com.dfinn.wallet.common.utils.staking
import com.dfinn.wallet.core.model.StorageEntry
import com.dfinn.wallet.core.storage.StorageCache
import com.dfinn.wallet.feature_staking_impl.data.network.blockhain.bindings.bindActiveEra
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.RuntimeMetadata
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigInteger

fun RuntimeMetadata.activeEraStorageKey() = staking().storage("ActiveEra").storageKey()

suspend fun StorageCache.observeActiveEraIndex(runtime: RuntimeSnapshot, chainId: String): Flow<BigInteger> {
    return observeEntry(runtime.metadata.activeEraStorageKey(), chainId)
        .map { bindActiveEra(it.content!!, runtime) }
}

suspend fun BulkRetriever.fetchValuesToCache(
    socketService: SocketService,
    keys: List<String>,
    storageCache: StorageCache,
    chainId: String,
) {
    val allValues = queryKeys(socketService, keys)

    val toInsert = allValues.map { (key, value) -> StorageEntry(key, value) }

    storageCache.insert(toInsert, chainId)
}

suspend fun BulkRetriever.fetchPrefixValuesToCache(
    socketService: SocketService,
    prefix: String,
    storageCache: StorageCache,
    chainId: String
) {
    val allKeys = retrieveAllKeys(socketService, prefix)

    fetchValuesToCache(socketService, allKeys, storageCache, chainId)
}
