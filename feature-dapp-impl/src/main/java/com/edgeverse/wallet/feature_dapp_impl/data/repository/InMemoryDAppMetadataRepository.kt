package com.edgeverse.wallet.feature_dapp_impl.data.repository

import com.edgeverse.wallet.common.utils.retryUntilDone
import com.edgeverse.wallet.feature_dapp_api.data.model.DappMetadata
import com.edgeverse.wallet.feature_dapp_api.data.repository.DAppMetadataRepository
import com.edgeverse.wallet.feature_dapp_impl.data.mappers.mapDAppMetadataResponseToDAppMetadatas
import com.edgeverse.wallet.feature_dapp_impl.data.network.metadata.DappMetadataApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class InMemoryDAppMetadataRepository(
    private val dappMetadataApi: DappMetadataApi,
    private val remoteApiUrl: String
) : DAppMetadataRepository {

    private val dappMetadatasFlow = MutableSharedFlow<List<DappMetadata>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun syncDAppMetadatas() {
        val response = retryUntilDone { dappMetadataApi.getParachainMetadata(remoteApiUrl) }
        val dappMetadatas = mapDAppMetadataResponseToDAppMetadatas(response)

        dappMetadatasFlow.emit(dappMetadatas)
    }

    override suspend fun getDAppMetadata(baseUrl: String): DappMetadata? {
        return dappMetadatasFlow.first().find { it.baseUrl == baseUrl }
    }

    override suspend fun findDAppMetadataByExactUrlMatch(fullUrl: String): DappMetadata? {
        return dappMetadatasFlow.first().find { it.url == fullUrl }
    }

    override suspend fun findDAppMetadatasByBaseUrlMatch(baseUrl: String): List<DappMetadata> {
        return dappMetadatasFlow.first().filter { it.baseUrl == baseUrl }
    }

    override suspend fun getDAppMetadatas(): List<DappMetadata> {
        return dappMetadatasFlow.first()
    }

    override fun observeDAppMetadatas(): Flow<List<DappMetadata>> {
        return dappMetadatasFlow
    }
}
