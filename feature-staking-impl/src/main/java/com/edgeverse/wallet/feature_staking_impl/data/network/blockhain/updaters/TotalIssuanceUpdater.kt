package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.edgeverse.wallet.common.utils.Modules
import com.edgeverse.wallet.common.utils.balances
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.GlobalScope
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleStorageKeyUpdater
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class TotalIssuanceUpdater(
    stakingSharedState: StakingSharedState,
    storageCache: StorageCache,
    chainRegistry: ChainRegistry
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, stakingSharedState, chainRegistry, storageCache) {

    override val requiredModules: List<String> = listOf(Modules.BALANCES)

    override suspend fun storageKey(runtime: RuntimeSnapshot): String {
        return runtime.metadata.balances().storage("TotalIssuance").storageKey()
    }
}
