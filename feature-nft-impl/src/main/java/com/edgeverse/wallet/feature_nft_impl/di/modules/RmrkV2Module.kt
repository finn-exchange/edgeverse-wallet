package com.edgeverse.wallet.feature_nft_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.dao.NftDao
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV2.RmrkV2NftProvider
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV2.network.kanaria.KanariaApi
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV2.network.singular.SingularV2Api
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module
class RmrkV2Module {

    @Provides
    @FeatureScope
    fun provideKanariaApi(networkApiCreator: NetworkApiCreator): KanariaApi {
        return networkApiCreator.create(KanariaApi::class.java, KanariaApi.BASE_URL)
    }

    @Provides
    @FeatureScope
    fun provideSingularApi(networkApiCreator: NetworkApiCreator): SingularV2Api {
        return networkApiCreator.create(SingularV2Api::class.java, SingularV2Api.BASE_URL)
    }

    @Provides
    @FeatureScope
    fun provideNftProvider(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        kanariaApi: KanariaApi,
        singularV2Api: SingularV2Api,
        nftDao: NftDao
    ) = RmrkV2NftProvider(chainRegistry, accountRepository, kanariaApi, singularV2Api, nftDao)
}
