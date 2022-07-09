package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.bindNumber
import com.edgeverse.wallet.common.data.network.runtime.binding.fromHexOrIncompatible
import com.edgeverse.wallet.common.data.network.runtime.binding.storageReturnType
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import java.math.BigInteger

@UseCaseBinding
fun bindHistoryDepth(scale: String, runtime: RuntimeSnapshot): BigInteger {
    val type = runtime.metadata.storageReturnType("Staking", "HistoryDepth")

    return bindNumber(type.fromHexOrIncompatible(scale, runtime))
}
