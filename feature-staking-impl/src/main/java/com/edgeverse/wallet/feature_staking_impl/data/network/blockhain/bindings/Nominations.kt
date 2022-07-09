package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.UseCaseBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.getList
import com.edgeverse.wallet.common.data.network.runtime.binding.getTyped
import com.edgeverse.wallet.common.data.network.runtime.binding.requireType
import com.edgeverse.wallet.common.data.network.runtime.binding.returnType
import com.edgeverse.wallet.common.utils.staking
import com.edgeverse.wallet.feature_staking_api.domain.model.Nominations
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHexOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage

@UseCaseBinding
fun bindNominations(scale: String, runtime: RuntimeSnapshot): Nominations {
    val type = runtime.metadata.staking().storage("Nominators").returnType()

    val dynamicInstance = type.fromHexOrNull(runtime, scale)
    requireType<Struct.Instance>(dynamicInstance)

    return Nominations(
        targets = dynamicInstance.getList("targets").map { it as AccountId },
        submittedInEra = bindEraIndex(dynamicInstance["submittedIn"]),
        suppressed = dynamicInstance.getTyped("suppressed")
    )
}
