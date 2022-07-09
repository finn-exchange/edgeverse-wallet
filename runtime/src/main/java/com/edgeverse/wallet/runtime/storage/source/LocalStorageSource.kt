package com.edgeverse.wallet.runtime.storage.source

import com.edgeverse.wallet.common.data.network.runtime.binding.BlockHash
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.storage.source.query.LocalStorageQueryContext
import com.edgeverse.wallet.runtime.storage.source.query.StorageQueryContext
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalStorageSource(
    chainRegistry: ChainRegistry,
    private val storageCache: StorageCache,
) : BaseStorageSource(chainRegistry) {

    override suspend fun query(key: String, chainId: String, at: BlockHash?): String? {
        requireWithoutAt(at)

        return storageCache.getEntry(key, chainId).content
    }

    override suspend fun observe(key: String, chainId: String): Flow<String?> {
        return storageCache.observeEntry(key, chainId)
            .map { it.content }
    }

    override suspend fun queryChildState(storageKey: String, childKey: String, chainId: String): String? {
        throw NotImplementedError("Child state queries are not yet supported in local storage")
    }

    override suspend fun createQueryContext(chainId: String, at: BlockHash?, runtime: RuntimeSnapshot): StorageQueryContext {
        return LocalStorageQueryContext(storageCache, chainId, at, runtime)
    }

    private fun requireWithoutAt(at: BlockHash?) = require(at == null) {
        "`At` parameter is not supported in local storage"
    }
}
