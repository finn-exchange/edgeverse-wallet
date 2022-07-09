package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.bindNumberConstant
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
