package com.edgeverse.wallet.runtime.network.updaters

import android.util.Log
import com.edgeverse.wallet.common.data.network.StorageSubscriptionBuilder
import com.edgeverse.wallet.common.utils.LOG_TAG
import com.edgeverse.wallet.common.utils.hasModule
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import com.edgeverse.wallet.runtime.multiNetwork.getSocket
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.selectedChainFlow
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.subscribeUsing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion

class SingleChainUpdateSystem(
    private val updaters: List<Updater>,
    private val chainRegistry: ChainRegistry,
    private val singleAssetSharedState: SingleAssetSharedState,
) : UpdateSystem {

    override fun start(): Flow<Updater.SideEffect> = singleAssetSharedState.selectedChainFlow().flatMapLatest { chain ->
        val socket = chainRegistry.getSocket(chain.id)
        val runtimeMetadata = chainRegistry.getRuntime(chain.id).metadata

        val logTag = this@SingleChainUpdateSystem.LOG_TAG
        val selfName = this@SingleChainUpdateSystem::class.java.simpleName

        val scopeFlows = updaters.groupBy(Updater::scope).map { (scope, scopeUpdaters) ->
            scope.invalidationFlow().flatMapLatest {
                val subscriptionBuilder = StorageSubscriptionBuilder.create(socket)

                val updatersFlow = scopeUpdaters
                    .filter { it.requiredModules.all(runtimeMetadata::hasModule) }
                    .map { updater ->
                        updater.listenForUpdates(subscriptionBuilder)
                            .catch { Log.e(logTag, "Failed to start $selfName for ${chain.name}: ${it.message}") }
                            .flowOn(Dispatchers.Default)
                    }

                if (updatersFlow.isNotEmpty()) {
                    val cancellable = socket.subscribeUsing(subscriptionBuilder.build())

                    updatersFlow.merge().onCompletion {
                        cancellable.cancel()
                    }
                } else {
                    emptyFlow()
                }
            }
        }

        scopeFlows.merge()
    }.flowOn(Dispatchers.Default)
}
