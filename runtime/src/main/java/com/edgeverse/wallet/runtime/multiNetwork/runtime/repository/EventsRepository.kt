package com.edgeverse.wallet.runtime.multiNetwork.runtime.repository

import com.edgeverse.wallet.common.data.network.runtime.binding.BlockHash
import com.edgeverse.wallet.common.data.network.runtime.binding.EventRecord
import com.edgeverse.wallet.common.data.network.runtime.binding.Phase
import com.edgeverse.wallet.common.data.network.runtime.binding.bindEventRecords
import com.edgeverse.wallet.common.utils.extrinsicHash
import com.edgeverse.wallet.common.utils.system
import com.edgeverse.wallet.runtime.extrinsic.create
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import com.edgeverse.wallet.runtime.network.rpc.RpcCalls
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import com.edgeverse.wallet.runtime.storage.source.queryNonNull
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHexOrNull
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.GenericEvent
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

interface EventsRepository {

    /**
     * @return events in block corresponding to [blockHash] or in current block, if [blockHash] is null
     * Unparsed events are not included
     */
    suspend fun getEventsInBlock(chainId: ChainId, blockHash: BlockHash? = null): List<EventRecord>

    /**
     * @return extrinsics with their event in block corresponding to [blockHash] or in current block, if [blockHash] is null
     * Unparsed events & extrinsics are not included
     */
    suspend fun getExtrinsicsWithEvents(chainId: ChainId, blockHash: BlockHash? = null): List<ExtrinsicWithEvents>
}

class RemoteEventsRepository(
    private val rpcCalls: RpcCalls,
    private val chainRegistry: ChainRegistry,
    private val remoteStorageSource: StorageDataSource
) : EventsRepository {

    override suspend fun getEventsInBlock(chainId: ChainId, blockHash: BlockHash?): List<EventRecord> {
        return remoteStorageSource.queryNonNull(
            chainId = chainId,
            keyBuilder = { it.metadata.system().storage("Events").storageKey() },
            binding = { scale, runtime ->
                bindEventRecords(scale, runtime)
            },
            at = blockHash
        )
    }

    override suspend fun getExtrinsicsWithEvents(
        chainId: ChainId,
        blockHash: BlockHash?
    ): List<ExtrinsicWithEvents> {
        val runtime = chainRegistry.getRuntime(chainId)
        val extrinsicType = Extrinsic.create(runtime)

        val block = rpcCalls.getBlock(chainId, blockHash)
        val events = getEventsInBlock(chainId, blockHash)

        val eventsByExtrinsicIndex: Map<Int, List<GenericEvent.Instance>> = events.mapNotNull { eventRecord ->
            (eventRecord.phase as? Phase.ApplyExtrinsic)?.let {
                it.extrinsicId.toInt() to eventRecord.event
            }
        }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )

        return block.block.extrinsics.mapIndexed { index, extrinsicScale ->
            val decodedExtrinsic = extrinsicType.fromHexOrNull(runtime, extrinsicScale)

            decodedExtrinsic?.let {
                val extrinsicEvents = eventsByExtrinsicIndex[index] ?: emptyList()

                ExtrinsicWithEvents(
                    extrinsic = decodedExtrinsic,
                    extrinsicHash = extrinsicScale.extrinsicHash(),
                    events = extrinsicEvents
                )
            }
        }.filterNotNull()
    }
}
