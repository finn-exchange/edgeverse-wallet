package com.edgeverse.wallet.runtime.repository

import com.edgeverse.wallet.common.data.network.runtime.binding.BlockNumber
import com.edgeverse.wallet.common.data.network.runtime.binding.bindBlockNumber
import com.edgeverse.wallet.common.utils.babe
import com.edgeverse.wallet.common.utils.babeOrNull
import com.edgeverse.wallet.common.utils.numberConstant
import com.edgeverse.wallet.common.utils.optionalNumberConstant
import com.edgeverse.wallet.common.utils.system
import com.edgeverse.wallet.common.utils.timestampOrNull
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import com.edgeverse.wallet.runtime.storage.source.observeNonNull
import com.edgeverse.wallet.runtime.storage.source.queryNonNull
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

class ChainStateRepository(
    private val localStorage: StorageDataSource,
    private val chainRegistry: ChainRegistry
) {

    suspend fun expectedBlockTimeInMillis(chainId: ChainId): BigInteger {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.babe().numberConstant("ExpectedBlockTime", runtime)
    }

    suspend fun expectedBlockTimeInMillisOrNull(chainId: ChainId): BigInteger? {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.babeOrNull()?.numberConstant("ExpectedBlockTime", runtime)
    }

    suspend fun minimumPeriodOrNull(chainId: ChainId): BigInteger? {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.timestampOrNull()?.numberConstant("MinimumPeriod", runtime)
    }

    suspend fun blockHashCount(chainId: ChainId): BigInteger? {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.system().optionalNumberConstant("BlockHashCount", runtime)
    }

    suspend fun currentBlock(chainId: ChainId) = localStorage.queryNonNull(
        keyBuilder = ::currentBlockStorageKey,
        binding = { scale, runtime -> bindBlockNumber(scale, runtime) },
        chainId = chainId
    )

    fun currentBlockNumberFlow(chainId: ChainId): Flow<BlockNumber> = localStorage.observeNonNull(
        keyBuilder = ::currentBlockStorageKey,
        binding = { scale, runtime -> bindBlockNumber(scale, runtime) },
        chainId = chainId
    )

    private fun currentBlockStorageKey(runtime: RuntimeSnapshot) = runtime.metadata.system().storage("Number").storageKey()
}
