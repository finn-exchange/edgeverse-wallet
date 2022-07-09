package com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding

import com.edgeverse.wallet.common.data.network.runtime.binding.BalanceOf
import com.edgeverse.wallet.common.data.network.runtime.binding.bindAccountId
import com.edgeverse.wallet.common.data.network.runtime.binding.bindNumber
import com.edgeverse.wallet.common.data.network.runtime.binding.cast
import com.edgeverse.wallet.common.data.network.runtime.binding.fromHexOrIncompatible
import com.edgeverse.wallet.common.data.network.runtime.binding.returnType
import com.edgeverse.wallet.common.utils.slots
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage

class LeaseEntry(
    val accountId: AccountId,
    val locked: BalanceOf
)

fun bindLeases(scale: String, runtimeSnapshot: RuntimeSnapshot): List<LeaseEntry?> {
    val type = runtimeSnapshot.metadata.slots().storage("Leases").returnType()

    val dynamicInstance = type.fromHexOrIncompatible(scale, runtimeSnapshot).cast<List<*>>()

    return dynamicInstance.map {
        it?.let {
            val (accountIdRaw, balanceRaw) = it.cast<List<*>>()

            LeaseEntry(
                accountId = bindAccountId(accountIdRaw),
                locked = bindNumber(balanceRaw)
            )
        }
    }
}
