package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.dfinn.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.dfinn.wallet.common.data.network.runtime.binding.bindNumberConstant
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.Constant
import java.math.BigInteger

/*
SlashDeferDuration = EraIndex
 */
@UseCaseBinding
fun bindSlashDeferDuration(
    constant: Constant,
    runtime: RuntimeSnapshot
): BigInteger = bindNumberConstant(constant, runtime)

@UseCaseBinding
fun bindMaximumRewardedNominators(
    constant: Constant,
    runtime: RuntimeSnapshot
): BigInteger = bindNumberConstant(constant, runtime)
