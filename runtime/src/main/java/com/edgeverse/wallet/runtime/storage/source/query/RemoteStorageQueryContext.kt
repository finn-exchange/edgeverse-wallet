package com.edgeverse.wallet.runtime.storage.source.query

import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.data.network.rpc.queryKey
import com.edgeverse.wallet.common.data.network.rpc.retrieveAllValues
import com.edgeverse.wallet.common.data.network.runtime.binding.BlockHash
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService

class RemoteStorageQueryContext(
    private val bulkRetriever: BulkRetriever,
    private val socketService: SocketService,
    at: BlockHash?,
    runtime: RuntimeSnapshot
) : BaseStorageQueryContext(runtime, at) {

    override suspend fun queryKeysByPrefix(prefix: String): List<String> {
        return bulkRetriever.retrieveAllKeys(socketService, prefix)
    }

    override suspend fun queryEntriesByPrefix(prefix: String): Map<String, String?> {
        return bulkRetriever.retrieveAllValues(socketService, prefix)
    }

    override suspend fun queryKeys(keys: List<String>, at: BlockHash?): Map<String, String?> {
        return bulkRetriever.queryKeys(socketService, keys, at)
    }

    override suspend fun queryKey(key: String, at: BlockHash?): String? {
        return bulkRetriever.queryKey(socketService, key, at)
    }
}
