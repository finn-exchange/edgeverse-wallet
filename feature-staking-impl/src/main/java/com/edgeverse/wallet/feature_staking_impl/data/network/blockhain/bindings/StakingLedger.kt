package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.HelperBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.getList
import com.edgeverse.wallet.common.data.network.runtime.binding.getTyped
import com.edgeverse.wallet.common.data.network.runtime.binding.incompatible
import com.edgeverse.wallet.common.data.network.runtime.binding.requireType
import com.edgeverse.wallet.common.data.network.runtime.binding.returnType
import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingLedger
import com.edgeverse.wallet.feature_staking_api.domain.model.UnlockChunk
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHexOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage

@UseCaseBinding
fun bindStakingLedger(scale: String, runtime: RuntimeSnapshot): StakingLedger {
    val type = runtime.metadata.staking().storage("Ledger").returnType()
    val dynamicInstance = type.fromHexOrNull(runtime, scale) ?: incompatible()
    requireType<Struct.Instance>(dynamicInstance)

    return StakingLedger(
        stashId = dynamicInstance.getTyped("stash"),
        total = dynamicInstance.getTyped("total"),
        active = dynamicInstance.getTyped("active"),
        unlocking = dynamicInstance.getList("unlocking").map(::bindUnlockChunk),
        claimedRewards = dynamicInstance.getList("claimedRewards").map(::bindEraIndex)
    )
}

@HelperBinding
fun bindUnlockChunk(dynamicInstance: Any?): UnlockChunk {
    requireType<Struct.Instance>(dynamicInstance)

    return UnlockChunk(
        amount = dynamicInstance.getTyped("value"),
        era = dynamicInstance.getTyped("era")
    )
}
