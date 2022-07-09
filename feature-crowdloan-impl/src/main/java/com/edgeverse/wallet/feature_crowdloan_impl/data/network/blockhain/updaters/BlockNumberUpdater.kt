package com.edgeverse.wallet.feature_crowdloan_impl.data.network.blockhain.updaters

import com.edgeverse.wallet.common.utils.Modules
import com.edgeverse.wallet.common.utils.system
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.GlobalScope
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleStorageKeyUpdater
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class BlockNumberUpdater(
    chainRegistry: ChainRegistry,
    crowdloanSharedState: CrowdloanSharedState,
    storageCache: StorageCache
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, crowdloanSharedState, chainRegistry, storageCache) {

    override suspend fun storageKey(runtime: RuntimeSnapshot): String {
        return runtime.metadata.system().storage("Number").storageKey()
    }

    override val requiredModules = listOf(Modules.SYSTEM)
}
