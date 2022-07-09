package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.getList
import com.edgeverse.wallet.common.data.network.runtime.binding.incompatible
import com.edgeverse.wallet.common.data.network.runtime.binding.storageReturnType
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.Type
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHexOrNull
import com.edgeverse.wallet.feature_staking_api.domain.model.SlashingSpans

@UseCaseBinding
fun bindSlashingSpans(
    scale: String,
    runtime: RuntimeSnapshot,
    returnType: Type<*> = runtime.metadata.storageReturnType("Staking", "SlashingSpans")
): SlashingSpans {
    val decoded = returnType.fromHexOrNull(runtime, scale) as? Struct.Instance ?: incompatible()

    return SlashingSpans(
        lastNonZeroSlash = bindEraIndex(decoded["lastNonzeroSlash"]),
        prior = decoded.getList("prior").map(::bindEraIndex)
    )
}
