package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.historical

import com.edgeverse.wallet.common.utils.staking
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import java.math.BigInteger

class HistoricalValidatorRewardPointsUpdater : HistoricalUpdater {

    override fun constructHistoricalKey(runtime: RuntimeSnapshot, era: BigInteger): String {
        return runtime.metadata.staking().storage("ErasRewardPoints").storageKey(runtime, era)
    }
}
