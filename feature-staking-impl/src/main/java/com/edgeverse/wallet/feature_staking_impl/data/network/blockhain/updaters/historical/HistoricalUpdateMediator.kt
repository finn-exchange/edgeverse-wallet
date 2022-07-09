package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.historical

import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.utils.Modules
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.GlobalScopeUpdater
import com.edgeverse.wallet.core.updater.SubscriptionBuilder
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_api.domain.api.historicalEras
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.fetchValuesToCache
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.observeActiveEraIndex
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.math.BigInteger

interface HistoricalUpdater {

    fun constructHistoricalKey(runtime: RuntimeSnapshot, era: BigInteger): String
}

class HistoricalUpdateMediator(
    private val historicalUpdaters: List<HistoricalUpdater>,
    private val stakingSharedState: StakingSharedState,
    private val bulkRetriever: BulkRetriever,
    private val stakingRepository: StakingRepository,
    private val storageCache: StorageCache,
    private val chainRegistry: ChainRegistry,
) : GlobalScopeUpdater {

    override val requiredModules: List<String> = listOf(Modules.STAKING)

    override suspend fun listenForUpdates(storageSubscriptionBuilder: SubscriptionBuilder): Flow<Updater.SideEffect> {
        val chainId = stakingSharedState.chainId()
        val runtime = chainRegistry.getRuntime(chainId)

        return storageCache.observeActiveEraIndex(runtime, chainId)
            .map {
                val allKeysNeeded = constructHistoricalKeys(chainId, runtime)
                val keysInDataBase = storageCache.filterKeysInCache(allKeysNeeded, chainId).toSet()

                val missingKeys = allKeysNeeded.filter { it !in keysInDataBase }

                missingKeys
            }.filter { it.isNotEmpty() }
            .onEach {
                bulkRetriever.fetchValuesToCache(storageSubscriptionBuilder.socketService, it, storageCache, chainId)
            }
            .noSideAffects()
    }

    private suspend fun constructHistoricalKeys(chainId: ChainId, runtime: RuntimeSnapshot): List<String> {
        val historicalRange = stakingRepository.historicalEras(chainId)

        return historicalUpdaters.map { updater ->
            historicalRange.map { updater.constructHistoricalKey(runtime, it) }
        }.flatten()
    }
}
