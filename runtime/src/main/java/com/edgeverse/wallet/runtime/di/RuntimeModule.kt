package com.edgeverse.wallet.runtime.di

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core_db.dao.ChainDao
import com.edgeverse.wallet.core_db.dao.StorageDao
import com.edgeverse.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.edgeverse.wallet.runtime.extrinsic.ExtrinsicSerializers
import com.edgeverse.wallet.runtime.extrinsic.MortalityConstructor
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.DbRuntimeVersionsRepository
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.RemoteEventsRepository
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
import com.edgeverse.wallet.runtime.network.rpc.RpcCalls
import com.edgeverse.wallet.runtime.repository.ChainStateRepository
import com.edgeverse.wallet.runtime.storage.DbStorageCache
import com.edgeverse.wallet.runtime.storage.source.LocalStorageSource
import com.edgeverse.wallet.runtime.storage.source.RemoteStorageSource
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import javax.inject.Named

const val LOCAL_STORAGE_SOURCE = "LOCAL_STORAGE_SOURCE"
const val REMOTE_STORAGE_SOURCE = "REMOTE_STORAGE_SOURCE"

@Module
class RuntimeModule {

    @Provides
    @ApplicationScope
    fun provideExtrinsicBuilderFactory(
        rpcCalls: RpcCalls,
        chainRegistry: ChainRegistry,
        mortalityConstructor: MortalityConstructor,
    ) = ExtrinsicBuilderFactory(
        rpcCalls,
        chainRegistry,
        mortalityConstructor
    )

    @Provides
    @ApplicationScope
    fun provideStorageCache(
        storageDao: StorageDao,
    ): StorageCache = DbStorageCache(storageDao)

    @Provides
    @Named(LOCAL_STORAGE_SOURCE)
    @ApplicationScope
    fun provideLocalStorageSource(
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ): StorageDataSource = LocalStorageSource(chainRegistry, storageCache)

    @Provides
    @Named(REMOTE_STORAGE_SOURCE)
    @ApplicationScope
    fun provideRemoteStorageSource(
        chainRegistry: ChainRegistry,
        bulkRetriever: BulkRetriever,
    ): StorageDataSource = RemoteStorageSource(chainRegistry, bulkRetriever)

    @Provides
    @ApplicationScope
    fun provideChainStateRepository(
        @Named(LOCAL_STORAGE_SOURCE) localStorageSource: StorageDataSource,
        chainRegistry: ChainRegistry
    ) = ChainStateRepository(localStorageSource, chainRegistry)

    @Provides
    @ApplicationScope
    fun provideMortalityProvider(
        chainStateRepository: ChainStateRepository,
        rpcCalls: RpcCalls,
    ) = MortalityConstructor(rpcCalls, chainStateRepository)

    @Provides
    @ApplicationScope
    fun provideSubstrateCalls(
        chainRegistry: ChainRegistry
    ) = RpcCalls(chainRegistry)

    @Provides
    @ApplicationScope
    @ExtrinsicSerialization
    fun provideExtrinsicGson() = ExtrinsicSerializers.gson()

    @Provides
    @ApplicationScope
    fun provideRuntimeVersionsRepository(
        chainDao: ChainDao
    ): RuntimeVersionsRepository = DbRuntimeVersionsRepository(chainDao)

    @Provides
    @ApplicationScope
    fun provideEventsRepository(
        rpcCalls: RpcCalls,
        chainRegistry: ChainRegistry,
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource
    ): EventsRepository = RemoteEventsRepository(rpcCalls, chainRegistry, remoteStorageSource)

    @Provides
    @ApplicationScope
    fun provideMultiChainQrSharingFactory() = MultiChainQrSharingFactory()
}
