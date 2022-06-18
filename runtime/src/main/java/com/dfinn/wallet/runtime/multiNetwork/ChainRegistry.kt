package com.dfinn.wallet.runtime.multiNetwork

import com.google.gson.Gson
import com.dfinn.wallet.common.utils.diffed
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.mapList
import com.dfinn.wallet.common.utils.removeHexPrefix
import com.dfinn.wallet.core_db.dao.ChainDao
import com.dfinn.wallet.runtime.multiNetwork.chain.ChainSyncService
import com.dfinn.wallet.runtime.multiNetwork.chain.mapChainLocalToChain
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.connection.ChainConnection
import com.dfinn.wallet.runtime.multiNetwork.connection.ConnectionPool
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeProvider
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeProviderPool
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeSubscriptionPool
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeSyncService
import com.dfinn.wallet.runtime.multiNetwork.runtime.types.BaseTypeSynchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

data class ChainService(
    val runtimeProvider: RuntimeProvider,
    val connection: ChainConnection
)

class ChainRegistry(
    private val runtimeProviderPool: RuntimeProviderPool,
    private val connectionPool: ConnectionPool,
    private val runtimeSubscriptionPool: RuntimeSubscriptionPool,
    private val chainDao: ChainDao,
    private val chainSyncService: ChainSyncService,
    private val baseTypeSynchronizer: BaseTypeSynchronizer,
    private val runtimeSyncService: RuntimeSyncService,
    private val gson: Gson
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    val currentChains = chainDao.joinChainInfoFlow()
        .mapList { mapChainLocalToChain(it, gson) }
        .diffed()
        .map { (removed, addedOrModified, all) ->
            removed.forEach {
                val chainId = it.id

                runtimeProviderPool.removeRuntimeProvider(chainId)
                runtimeSubscriptionPool.removeSubscription(chainId)
                runtimeSyncService.unregisterChain(chainId)
                connectionPool.removeConnection(chainId)
            }

            addedOrModified.forEach { chain ->
                val connection = connectionPool.setupConnection(chain)

                runtimeProviderPool.setupRuntimeProvider(chain)
                runtimeSyncService.registerChain(chain, connection)
                runtimeSubscriptionPool.setupRuntimeSubscription(chain, connection)
                runtimeProviderPool.setupRuntimeProvider(chain)
            }

            all
        }
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .inBackground()
        .shareIn(this, SharingStarted.Eagerly, replay = 1)

    val chainsById = currentChains.map { chains -> chains.associateBy { it.id } }
        .inBackground()
        .shareIn(this, SharingStarted.Eagerly, replay = 1)

    init {
        launch { chainSyncService.syncUp() }

        baseTypeSynchronizer.sync()
    }

    fun getConnection(chainId: String) = connectionPool.getConnection(chainId.removeHexPrefix())

    fun getRuntimeProvider(chainId: String) = runtimeProviderPool.getRuntimeProvider(chainId.removeHexPrefix())

    suspend fun getChain(chainId: String): Chain = chainsById.first().getValue(chainId.removeHexPrefix())
}

suspend fun ChainRegistry.getChainOrNull(chainId: String): Chain? {
    return chainsById.first()[chainId.removeHexPrefix()]
}

suspend fun ChainRegistry.chainWithAssetOrNull(chainId: String, assetId: Int): Pair<Chain, Chain.Asset>? {
    val chain = getChainOrNull(chainId) ?: return null
    val chainAsset = chain.assetsById[assetId] ?: return null

    return chain to chainAsset
}

suspend fun ChainRegistry.chainWithAsset(chainId: String, assetId: Int): Pair<Chain, Chain.Asset> {
    val chain = chainsById.first().getValue(chainId)

    return chain to chain.assetsById.getValue(assetId)
}

suspend fun ChainRegistry.asset(chainId: String, assetId: Int): Chain.Asset {
    val chain = chainsById.first().getValue(chainId)

    return chain.assetsById.getValue(assetId)
}

suspend inline fun ChainRegistry.findChain(predicate: (Chain) -> Boolean): Chain? = currentChains.first().firstOrNull(predicate)

suspend fun ChainRegistry.getRuntime(chainId: String) = getRuntimeProvider(chainId).get()

fun ChainRegistry.getSocket(chainId: String) = getConnection(chainId).socketService

fun ChainRegistry.getService(chainId: String) = ChainService(
    runtimeProvider = getRuntimeProvider(chainId),
    connection = getConnection(chainId)
)
