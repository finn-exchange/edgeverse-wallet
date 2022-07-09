package com.edgeverse.wallet.common.data.network.runtime.binding

import com.edgeverse.wallet.common.utils.system
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import java.math.BigInteger

typealias BlockNumber = BigInteger

typealias BlockHash = String

fun bindBlockNumber(scale: String, runtime: RuntimeSnapshot): BlockNumber {
    val type = runtime.metadata.system().storage("Number").returnType()

    val dynamicInstance = type.fromHexOrIncompatible(scale, runtime)

    return bindNumber(dynamicInstance)
}
