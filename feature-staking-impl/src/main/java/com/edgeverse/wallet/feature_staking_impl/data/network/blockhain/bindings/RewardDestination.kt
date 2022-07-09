package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.cast
import com.edgeverse.wallet.common.data.network.runtime.binding.incompatible
import com.edgeverse.wallet.common.data.network.runtime.binding.returnType
import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.feature_staking_api.domain.model.RewardDestination
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHexOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage

private const val TYPE_STAKED = "Staked"
private const val TYPE_ACCOUNT = "Account"

fun bindRewardDestination(rewardDestination: RewardDestination) = when (rewardDestination) {
    is RewardDestination.Restake -> DictEnum.Entry(TYPE_STAKED, null)
    is RewardDestination.Payout -> DictEnum.Entry(TYPE_ACCOUNT, rewardDestination.targetAccountId)
}

fun bindRewardDestination(
    scale: String,
    runtime: RuntimeSnapshot,
    stashId: AccountId,
    controllerId: AccountId,
): RewardDestination {
    val type = runtime.metadata.staking().storage("Payee").returnType()

    val dynamicInstance = type.fromHexOrNull(runtime, scale).cast<DictEnum.Entry<*>>()

    return when (dynamicInstance.name) {
        TYPE_STAKED -> RewardDestination.Restake
        TYPE_ACCOUNT -> RewardDestination.Payout(dynamicInstance.value.cast())
        "Stash" -> RewardDestination.Payout(stashId)
        "Controller" -> RewardDestination.Payout(controllerId)
        else -> incompatible()
    }
}
