package com.dfinn.wallet.feature_dapp_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.dao.PhishingSitesDao
import com.dfinn.wallet.feature_dapp_impl.data.network.phishing.PhishingSitesApi
import com.dfinn.wallet.feature_dapp_impl.data.repository.PhishingSitesRepository
import com.dfinn.wallet.feature_dapp_impl.data.repository.PhishingSitesRepositoryImpl

@Module
class PhishingSitesModule {

    @Provides
    @FeatureScope
    fun providePhishingSitesApi(networkApiCreator: NetworkApiCreator): PhishingSitesApi {
        return networkApiCreator.create(PhishingSitesApi::class.java)
    }

    @Provides
    @FeatureScope
    fun providePhishingSitesRepository(
        api: PhishingSitesApi,
        phishingSitesDao: PhishingSitesDao
    ): PhishingSitesRepository = PhishingSitesRepositoryImpl(phishingSitesDao, api)
}
