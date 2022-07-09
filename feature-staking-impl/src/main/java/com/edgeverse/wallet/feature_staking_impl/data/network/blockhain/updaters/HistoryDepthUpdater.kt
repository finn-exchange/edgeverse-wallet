package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.edgeverse.wallet.common.utils.defaultInHex
import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.GlobalScope
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.base.StakingUpdater
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleStorageKeyUpdater
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class HistoryDepthUpdater(
    stakingSharedState: StakingSharedState,
    chainRegistry: ChainRegistry,
    storageCache: StorageCache,
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, stakingSharedState, chainRegistry, storageCache), StakingUpdater {

    override fun fallbackValue(runtime: RuntimeSnapshot): String {
        return storageEntry(runtime).defaultInHex()
    }

    override suspend fun storageKey(runtime: RuntimeSnapshot): String {
        return storageEntry(runtime).storageKey()
    }

    private fun storageEntry(runtime: RuntimeSnapshot) = runtime.metadata.staking().storage("HistoryDepth")
}
