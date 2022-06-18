package com.dfinn.wallet.feature_nft_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.dao.NftDao
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_nft_impl.data.source.providers.rmrkV2.RmrkV2NftProvider
import com.dfinn.wallet.feature_nft_impl.data.source.providers.rmrkV2.network.kanaria.KanariaApi
import com.dfinn.wallet.feature_nft_impl.data.source.providers.rmrkV2.network.singular.SingularV2Api
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

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
