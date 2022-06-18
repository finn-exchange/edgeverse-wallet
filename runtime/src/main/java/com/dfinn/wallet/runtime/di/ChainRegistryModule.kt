package com.dfinn.wallet.runtime.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.common.interfaces.FileProvider
import com.dfinn.wallet.core_db.dao.ChainDao
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.ChainSyncService
import com.dfinn.wallet.runtime.multiNetwork.chain.remote.ChainFetcher
import com.dfinn.wallet.runtime.multiNetwork.connection.ChainConnection
import com.dfinn.wallet.runtime.multiNetwork.connection.ChainConnectionFactory
import com.dfinn.wallet.runtime.multiNetwork.connection.ConnectionPool
import com.dfinn.wallet.runtime.multiNetwork.connection.autobalance.NodeAutobalancer
import com.dfinn.wallet.runtime.multiNetwork.connection.autobalance.strategy.AutoBalanceStrategyProvider
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeFactory
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeFilesCache
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeProviderPool
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeSubscriptionPool
import com.dfinn.wallet.runtime.multiNetwork.runtime.RuntimeSyncService
import com.dfinn.wallet.runtime.multiNetwork.runtime.types.BaseTypeSynchronizer
import com.dfinn.wallet.runtime.multiNetwork.runtime.types.TypesFetcher
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Provider

@Module
class ChainRegistryModule {

    @Provides
    @ApplicationScope
    fun provideChainFetcher(apiCreator: NetworkApiCreator) = apiCreator.create(ChainFetcher::class.java)

    @Provides
    @ApplicationScope
    fun provideChainSyncService(
        dao: ChainDao,
        chainFetcher: ChainFetcher,
        gson: Gson
    ) = ChainSyncService(dao, chainFetcher, gson)

    @Provides
    @ApplicationScope
    fun provideRuntimeFactory(
        runtimeFilesCache: RuntimeFilesCache,
        chainDao: ChainDao,
        gson: Gson,
    ): RuntimeFactory {
        return RuntimeFactory(runtimeFilesCache, chainDao, gson)
    }

    @Provides
    @ApplicationScope
    fun provideRuntimeFilesCache(
        fileProvider: FileProvider,
    ) = RuntimeFilesCache(fileProvider)

    @Provides
    @ApplicationScope
    fun provideTypesFetcher(
        networkApiCreator: NetworkApiCreator,
    ) = networkApiCreator.create(TypesFetcher::class.java)

    @Provides
    @ApplicationScope
    fun provideRuntimeSyncService(
        typesFetcher: TypesFetcher,
        runtimeFilesCache: RuntimeFilesCache,
        chainDao: ChainDao,
    ) = RuntimeSyncService(typesFetcher, runtimeFilesCache, chainDao)

    @Provides
    @ApplicationScope
    fun provideBaseTypeSynchronizer(
        typesFetcher: TypesFetcher,
        runtimeFilesCache: RuntimeFilesCache,
    ) = BaseTypeSynchronizer(runtimeFilesCache, typesFetcher)

    @Provides
    @ApplicationScope
    fun provideRuntimeProviderPool(
        runtimeFactory: RuntimeFactory,
        runtimeSyncService: RuntimeSyncService,
        baseTypeSynchronizer: BaseTypeSynchronizer,
    ) = RuntimeProviderPool(runtimeFactory, runtimeSyncService, baseTypeSynchronizer)

    @Provides
    @ApplicationScope
    fun provideAutoBalanceProvider() = AutoBalanceStrategyProvider()

    @Provides
    @ApplicationScope
    fun provideNodeAutoBalancer(
        autoBalanceStrategyProvider: AutoBalanceStrategyProvider,
    ) = NodeAutobalancer(autoBalanceStrategyProvider)

    @Provides
    @ApplicationScope
    fun provideChainConnectionFactory(
        socketProvider: Provider<SocketService>,
        externalRequirementsFlow: MutableStateFlow<ChainConnection.ExternalRequirement>,
        nodeAutobalancer: NodeAutobalancer,
    ) = ChainConnectionFactory(
        externalRequirementsFlow,
        nodeAutobalancer,
        socketProvider
    )

    @Provides
    @ApplicationScope
    fun provideConnectionPool(chainConnectionFactory: ChainConnectionFactory) = ConnectionPool(chainConnectionFactory)

    @Provides
    @ApplicationScope
    fun provideRuntimeVersionSubscriptionPool(
        chainDao: ChainDao,
        runtimeSyncService: RuntimeSyncService,
    ) = RuntimeSubscriptionPool(chainDao, runtimeSyncService)

    @Provides
    @ApplicationScope
    fun provideExternalRequirementsFlow() = MutableStateFlow(ChainConnection.ExternalRequirement.FORBIDDEN)

    @Provides
    @ApplicationScope
    fun provideChainRegistry(
        runtimeProviderPool: RuntimeProviderPool,
        chainConnectionPool: ConnectionPool,
        runtimeSubscriptionPool: RuntimeSubscriptionPool,
        chainDao: ChainDao,
        chainSyncService: ChainSyncService,
        baseTypeSynchronizer: BaseTypeSynchronizer,
        runtimeSyncService: RuntimeSyncService,
        gson: Gson
    ) = ChainRegistry(
        runtimeProviderPool,
        chainConnectionPool,
        runtimeSubscriptionPool,
        chainDao,
        chainSyncService,
        baseTypeSynchronizer,
        runtimeSyncService,
        gson
    )
}
