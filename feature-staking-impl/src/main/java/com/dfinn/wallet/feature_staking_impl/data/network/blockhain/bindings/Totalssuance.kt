package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.dfinn.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.dfinn.wallet.common.data.network.runtime.binding.bindNumber
import com.dfinn.wallet.common.data.network.runtime.binding.fromHexOrIncompatible
import com.dfinn.wallet.common.data.network.runtime.binding.storageReturnType
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import java.math.BigInteger

/*
Balance
 */
@UseCaseBinding
fun bindTotalInsurance(
    scale: String,
    runtime: RuntimeSnapshot
): BigInteger {
    val returnType = runtime.metadata.storageReturnType("Balances", "TotalIssuance")

    return bindNumber(returnType.fromHexOrIncompatible(scale, runtime))
}
