package com.edgeverse.wallet.runtime.di

import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.ChainSyncService
import com.edgeverse.wallet.runtime.multiNetwork.connection.ChainConnection
import com.edgeverse.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
import com.edgeverse.wallet.runtime.network.rpc.RpcCalls
import com.edgeverse.wallet.runtime.repository.ChainStateRepository
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ExtrinsicSerialization

interface RuntimeApi {

    fun provideExtrinsicBuilderFactory(): ExtrinsicBuilderFactory

    fun externalRequirementFlow(): MutableStateFlow<ChainConnection.ExternalRequirement>

    fun storageCache(): StorageCache

    @Named(REMOTE_STORAGE_SOURCE)
    fun remoteStorageSource(): StorageDataSource

    @Named(LOCAL_STORAGE_SOURCE)
    fun localStorageSource(): StorageDataSource

    fun chainSyncService(): ChainSyncService

    fun chainStateRepository(): ChainStateRepository

    fun chainRegistry(): ChainRegistry

    fun rpcCalls(): RpcCalls

    @ExtrinsicSerialization
    fun extrinsicGson(): Gson

    fun runtimeVersionsRepository(): RuntimeVersionsRepository

    fun eventsRepository(): EventsRepository

    val multiChainQrSharingFactory: MultiChainQrSharingFactory
}
