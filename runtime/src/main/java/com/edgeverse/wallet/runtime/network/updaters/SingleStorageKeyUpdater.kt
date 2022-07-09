package com.edgeverse.wallet.runtime.network.updaters

import com.edgeverse.wallet.common.data.holders.ChainIdHolder
import com.edgeverse.wallet.core.model.StorageChange
import com.edgeverse.wallet.core.model.StorageEntry
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.SubscriptionBuilder
import com.edgeverse.wallet.core.updater.UpdateScope
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

suspend fun StorageCache.insert(
    storageChange: StorageChange,
    chainId: String,
) {
    val storageEntry = StorageEntry(
        storageKey = storageChange.key,
        content = storageChange.value,
    )

    insert(storageEntry, chainId)
}

abstract class SingleStorageKeyUpdater<S : UpdateScope>(
    override val scope: S,
    private val chainIdHolder: ChainIdHolder,
    private val chainRegistry: ChainRegistry,
    private val storageCache: StorageCache
) : Updater {

    /**
     * @return a storage key to update. null in case updater does not want to update anything
     */
    abstract suspend fun storageKey(runtime: RuntimeSnapshot): String?

    protected open fun fallbackValue(runtime: RuntimeSnapshot): String? = null

    override suspend fun listenForUpdates(storageSubscriptionBuilder: SubscriptionBuilder): Flow<Updater.SideEffect> {
        val chainId = chainIdHolder.chainId()
        val runtime = chainRegistry.getRuntime(chainId)

        val storageKey = storageKey(runtime) ?: return emptyFlow()

        return storageSubscriptionBuilder.subscribe(storageKey)
            .map {
                if (it.value == null) {
                    it.copy(value = fallbackValue(runtime))
                } else {
                    it
                }
            }
            .onEach { storageCache.insert(it, chainId) }
            .noSideAffects()
    }
}
