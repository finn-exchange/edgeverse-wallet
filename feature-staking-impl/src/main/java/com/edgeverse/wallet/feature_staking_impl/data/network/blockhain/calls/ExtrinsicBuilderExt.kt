package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.calls

import com.edgeverse.wallet.common.data.network.runtime.binding.MultiAddress
import com.edgeverse.wallet.common.data.network.runtime.binding.bindMultiAddress
import com.edgeverse.wallet.feature_staking_api.domain.model.RewardDestination
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings.bindRewardDestination
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import java.math.BigInteger

fun ExtrinsicBuilder.setController(controllerAddress: MultiAddress): ExtrinsicBuilder {
    return call(
        "Staking", "set_controller",
        mapOf(
            "controller" to bindMultiAddress(controllerAddress)
        )
    )
}

fun ExtrinsicBuilder.bond(
    controllerAddress: MultiAddress,
    amount: BigInteger,
    payee: RewardDestination,
): ExtrinsicBuilder {
    return call(
        "Staking", "bond",
        mapOf(
            "controller" to bindMultiAddress(controllerAddress),
            "value" to amount,
            "payee" to bindRewardDestination(payee)
        )
    )
}

fun ExtrinsicBuilder.nominate(targets: List<MultiAddress>): ExtrinsicBuilder {
    return call(
        "Staking", "nominate",
        mapOf(
            "targets" to targets.map(::bindMultiAddress)
        )
    )
}

fun ExtrinsicBuilder.payoutStakers(era: BigInteger, validatorId: AccountId): ExtrinsicBuilder {
    return call(
        "Staking", "payout_stakers",
        mapOf(
            "validator_stash" to validatorId,
            "era" to era
        )
    )
}

fun ExtrinsicBuilder.bondMore(amount: BigInteger): ExtrinsicBuilder {
    return call(
        "Staking", "bond_extra",
        mapOf(
            "max_additional" to amount
        )
    )
}

fun ExtrinsicBuilder.chill(): ExtrinsicBuilder {
    return call("Staking", "chill", emptyMap())
}

fun ExtrinsicBuilder.unbond(amount: BigInteger): ExtrinsicBuilder {
    return call(
        "Staking", "unbond",
        mapOf(
            "value" to amount
        )
    )
}

fun ExtrinsicBuilder.withdrawUnbonded(numberOfSlashingSpans: BigInteger): ExtrinsicBuilder {
    return call(
        "Staking", "withdraw_unbonded",
        mapOf(
            "num_slashing_spans" to numberOfSlashingSpans
        )
    )
}

fun ExtrinsicBuilder.rebond(amount: BigInteger): ExtrinsicBuilder {
    return call(
        "Staking", "rebond",
        mapOf(
            "value" to amount
        )
    )
}

fun ExtrinsicBuilder.setPayee(rewardDestination: RewardDestination): ExtrinsicBuilder {

    return call(
        "Staking", "set_payee",
        mapOf(
            "payee" to bindRewardDestination(rewardDestination)
        )
    )
}
