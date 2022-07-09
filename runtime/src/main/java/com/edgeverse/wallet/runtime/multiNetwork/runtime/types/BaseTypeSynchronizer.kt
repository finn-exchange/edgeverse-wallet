package com.edgeverse.wallet.runtime.multiNetwork.runtime.types

import com.edgeverse.wallet.common.utils.md5
import com.edgeverse.wallet.common.utils.retryUntilDone
import com.edgeverse.wallet.runtime.multiNetwork.runtime.FileHash
import com.edgeverse.wallet.runtime.multiNetwork.runtime.RuntimeFilesCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class BaseTypeSynchronizer(
    private val runtimeFilesCache: RuntimeFilesCache,
    private val typesFetcher: TypesFetcher,
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    @Volatile
    private var syncJob: Job? = null

    private val _syncStatusFlow = MutableSharedFlow<FileHash>()
    val syncStatusFlow: Flow<FileHash> = _syncStatusFlow

    @Synchronized
    fun sync() {
        syncJob?.cancel()

        syncJob = launch {
            retryUntilDone {
                val definitions = typesFetcher.getBaseTypes()

                runtimeFilesCache.saveBaseTypes(definitions)

                _syncStatusFlow.emit(definitions.md5())

                syncJob = null
            }
        }
    }

    @Synchronized
    fun cacheNotFound() {
        if (syncJob == null) {
            sync()
        }
    }
}
