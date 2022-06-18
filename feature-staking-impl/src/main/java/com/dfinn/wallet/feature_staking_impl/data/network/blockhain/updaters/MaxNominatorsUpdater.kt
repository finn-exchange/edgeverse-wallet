package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.dfinn.wallet.common.utils.defaultInHex
import com.dfinn.wallet.common.utils.staking
import com.dfinn.wallet.core.storage.StorageCache
import com.dfinn.wallet.core.updater.GlobalScope
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.data.network.blockhain.updaters.base.StakingUpdater
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.network.updaters.SingleStorageKeyUpdater
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageOrNull

class MaxNominatorsUpdater(
    storageCache: StorageCache,
    stakingSharedState: StakingSharedState,
    chainRegistry: ChainRegistry,
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, stakingSharedState, chainRegistry, storageCache), StakingUpdater {

    override suspend fun storageKey(runtime: RuntimeSnapshot): String? {
        return runtime.metadata.staking().storageOrNull("MaxNominatorsCount")?.storageKey()
    }

    override fun fallbackValue(runtime: RuntimeSnapshot): String? {
        return runtime.metadata.staking().storageOrNull("MaxNominatorsCount")?.defaultInHex()
    }
}
