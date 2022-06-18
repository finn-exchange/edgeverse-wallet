package com.dfinn.wallet.runtime.di

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.rpc.BulkRetriever
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.core.storage.StorageCache
import com.dfinn.wallet.core_db.dao.ChainDao
import com.dfinn.wallet.core_db.dao.StorageDao
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicSerializers
import com.dfinn.wallet.runtime.extrinsic.MortalityConstructor
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.DbRuntimeVersionsRepository
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.RemoteEventsRepository
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
import com.dfinn.wallet.runtime.network.rpc.RpcCalls
import com.dfinn.wallet.runtime.repository.ChainStateRepository
import com.dfinn.wallet.runtime.storage.DbStorageCache
import com.dfinn.wallet.runtime.storage.source.LocalStorageSource
import com.dfinn.wallet.runtime.storage.source.RemoteStorageSource
import com.dfinn.wallet.runtime.storage.source.StorageDataSource
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
