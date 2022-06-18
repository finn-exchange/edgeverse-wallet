package com.dfinn.wallet.feature_nft_impl.di

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.dao.NftDao
import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository
import com.dfinn.wallet.feature_nft_impl.data.repository.NftRepositoryImpl
import com.dfinn.wallet.feature_nft_impl.data.source.JobOrchestrator
import com.dfinn.wallet.feature_nft_impl.data.source.NftProvidersRegistry
import com.dfinn.wallet.feature_nft_impl.data.source.providers.rmrkV1.RmrkV1NftProvider
import com.dfinn.wallet.feature_nft_impl.data.source.providers.rmrkV2.RmrkV2NftProvider
import com.dfinn.wallet.feature_nft_impl.data.source.providers.uniques.UniquesNftProvider
import com.dfinn.wallet.feature_nft_impl.di.modules.RmrkV1Module
import com.dfinn.wallet.feature_nft_impl.di.modules.RmrkV2Module
import com.dfinn.wallet.feature_nft_impl.di.modules.UniquesModule
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

@Module(
    includes = [
        UniquesModule::class,
        RmrkV1Module::class,
        RmrkV2Module::class
    ]
)
class NftFeatureModule {

    @Provides
    @FeatureScope
    fun provideJobOrchestrator() = JobOrchestrator()

    @Provides
    @FeatureScope
    fun provideNftProviderRegistry(
        uniquesNftProvider: UniquesNftProvider,
        rmrkV1NftProvider: RmrkV1NftProvider,
        rmrkV2NftProvider: RmrkV2NftProvider,
    ) = NftProvidersRegistry(uniquesNftProvider, rmrkV1NftProvider, rmrkV2NftProvider)

    @Provides
    @FeatureScope
    fun provideNftRepository(
        nftProvidersRegistry: NftProvidersRegistry,
        chainRegistry: ChainRegistry,
        jobOrchestrator: JobOrchestrator,
        nftDao: NftDao,
        httpExceptionHandler: HttpExceptionHandler,
    ): NftRepository = NftRepositoryImpl(
        nftProvidersRegistry = nftProvidersRegistry,
        chainRegistry = chainRegistry,
        jobOrchestrator = jobOrchestrator,
        nftDao = nftDao,
        exceptionHandler = httpExceptionHandler
    )
}
