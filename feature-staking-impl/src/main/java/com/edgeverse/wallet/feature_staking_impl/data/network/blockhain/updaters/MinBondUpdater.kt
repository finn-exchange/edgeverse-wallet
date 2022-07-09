package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.GlobalScope
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.base.StakingUpdater
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleStorageKeyUpdater
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageOrNull

class MinBondUpdater(
    stakingSharedState: StakingSharedState,
    chainRegistry: ChainRegistry,
    storageCache: StorageCache,
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, stakingSharedState, chainRegistry, storageCache), StakingUpdater {

    override suspend fun storageKey(runtime: RuntimeSnapshot): String? {
        return runtime.metadata.staking().storageOrNull("MinNominatorBond")?.storageKey()
    }
}
