package com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding

import com.edgeverse.wallet.common.data.network.runtime.binding.bindNumber
import com.edgeverse.wallet.common.data.network.runtime.binding.bindString
import com.edgeverse.wallet.common.data.network.runtime.binding.cast
import com.edgeverse.wallet.common.data.network.runtime.binding.incompatible
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import java.math.BigInteger

class DirectContribution(
    val amount: BigInteger,
    val memo: String,
)

fun bindContribution(scale: String, runtime: RuntimeSnapshot): DirectContribution {
    val type = runtime.typeRegistry["(Balance, Vec<u8>)"] ?: incompatible()

    val dynamicInstance = type.fromHex(runtime, scale).cast<List<*>>()

    return DirectContribution(
        amount = bindNumber(dynamicInstance[0]),
        memo = bindString(dynamicInstance[1])
    )
}
