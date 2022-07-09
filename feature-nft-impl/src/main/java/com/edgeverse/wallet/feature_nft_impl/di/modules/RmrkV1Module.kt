package com.edgeverse.wallet.feature_nft_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.dao.NftDao
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV1.RmrkV1NftProvider
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV1.network.RmrkV1Api
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module
class RmrkV1Module {

    @Provides
    @FeatureScope
    fun provideApi(networkApiCreator: NetworkApiCreator): RmrkV1Api {
        return networkApiCreator.create(RmrkV1Api::class.java, RmrkV1Api.BASE_URL)
    }

    @Provides
    @FeatureScope
    fun provideNftProvider(
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
        api: RmrkV1Api,
        nftDao: NftDao
    ) = RmrkV1NftProvider(chainRegistry, accountRepository, api, nftDao)
}
