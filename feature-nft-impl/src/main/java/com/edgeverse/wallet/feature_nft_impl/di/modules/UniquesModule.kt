package com.edgeverse.wallet.feature_nft_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.dao.NftDao
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.uniques.UniquesNftProvider
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.uniques.network.IpfsApi
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import javax.inject.Named

@Module
class UniquesModule {

    @Provides
    @FeatureScope
    fun provideIpfsApi(networkApiCreator: NetworkApiCreator) = networkApiCreator.create(IpfsApi::class.java)

    @Provides
    @FeatureScope
    fun provideUniquesNftProvider(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource,
        nftDao: NftDao,
        ipfsApi: IpfsApi,
    ) = UniquesNftProvider(remoteStorageSource, accountRepository, chainRegistry, nftDao, ipfsApi)
}
