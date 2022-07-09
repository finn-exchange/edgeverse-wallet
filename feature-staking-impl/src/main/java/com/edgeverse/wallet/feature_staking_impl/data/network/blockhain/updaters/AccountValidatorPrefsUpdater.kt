package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.base.StakingUpdater
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.scope.AccountStakingScope
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleStorageKeyUpdater
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class AccountValidatorPrefsUpdater(
    scope: AccountStakingScope,
    storageCache: StorageCache,
    stakingSharedState: StakingSharedState,
    chainRegistry: ChainRegistry,
) : SingleStorageKeyUpdater<AccountStakingScope>(scope, stakingSharedState, chainRegistry, storageCache), StakingUpdater {

    override suspend fun storageKey(runtime: RuntimeSnapshot): String? {
        val stakingAccessInfo = scope.getAccountStaking().stakingAccessInfo ?: return null
        val stashId = stakingAccessInfo.stashId

        return runtime.metadata.staking().storage("Validators").storageKey(runtime, stashId)
    }
}
