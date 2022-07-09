package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.GlobalScopeUpdater
import com.edgeverse.wallet.core.updater.SubscriptionBuilder
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.base.StakingUpdater
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.math.BigInteger

class ValidatorExposureUpdater(
    private val bulkRetriever: BulkRetriever,
    private val stakingSharedState: StakingSharedState,
    private val chainRegistry: ChainRegistry,
    private val storageCache: StorageCache
) : GlobalScopeUpdater, StakingUpdater {

    override suspend fun listenForUpdates(storageSubscriptionBuilder: SubscriptionBuilder): Flow<Updater.SideEffect> {
        val chainId = stakingSharedState.chainId()
        val runtime = chainRegistry.getRuntime(chainId)

        return storageCache.observeActiveEraIndex(runtime, chainId)
            .map { eraStakersPrefix(runtime, it) }
            .filterNot { storageCache.isPrefixInCache(it, chainId) }
            .onEach { updateNominatorsForEra(it, storageSubscriptionBuilder.socketService, chainId) }
            .flowOn(Dispatchers.IO)
            .noSideAffects()
    }

    private fun eraStakersPrefix(runtime: RuntimeSnapshot, activeEraIndex: BigInteger): String {
        return runtime.metadata.staking().storage("ErasStakers").storageKey(runtime, activeEraIndex)
    }

    private suspend fun updateNominatorsForEra(
        eraStakersPrefix: String,
        socketService: SocketService,
        chainId: String
    ) = runCatching {
        bulkRetriever.fetchPrefixValuesToCache(socketService, eraStakersPrefix, storageCache, chainId)
    }
}
