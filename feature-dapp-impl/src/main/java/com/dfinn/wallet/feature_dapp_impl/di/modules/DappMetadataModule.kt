package com.dfinn.wallet.feature_dapp_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_dapp_api.data.repository.DAppMetadataRepository
import com.dfinn.wallet.feature_dapp_impl.BuildConfig
import com.dfinn.wallet.feature_dapp_impl.data.network.metadata.DappMetadataApi
import com.dfinn.wallet.feature_dapp_impl.data.repository.InMemoryDAppMetadataRepository

@Module
class DappMetadataModule {

    @Provides
    @FeatureScope
    fun provideApi(
        apiCreator: NetworkApiCreator
    ) = apiCreator.create(DappMetadataApi::class.java)

    @Provides
    @FeatureScope
    fun provideDRepository(
        api: DappMetadataApi
    ): DAppMetadataRepository = InMemoryDAppMetadataRepository(
        dappMetadataApi = api,
        remoteApiUrl = BuildConfig.DAPP_METADATAS_URL
    )
}
