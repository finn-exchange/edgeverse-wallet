package com.dfinn.wallet.feature_dapp_impl.di

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_dapp_api.data.repository.DAppMetadataRepository
import com.dfinn.wallet.feature_dapp_impl.data.repository.FavouritesDAppRepository
import com.dfinn.wallet.feature_dapp_impl.data.repository.PhishingSitesRepository
import com.dfinn.wallet.feature_dapp_impl.di.modules.DappMetadataModule
import com.dfinn.wallet.feature_dapp_impl.di.modules.FavouritesDAppModule
import com.dfinn.wallet.feature_dapp_impl.di.modules.PhishingSitesModule
import com.dfinn.wallet.feature_dapp_impl.di.modules.Web3Module
import com.dfinn.wallet.feature_dapp_impl.domain.DappInteractor

@Module(includes = [Web3Module::class, DappMetadataModule::class, PhishingSitesModule::class, FavouritesDAppModule::class])
class DappFeatureModule {

    @Provides
    @FeatureScope
    fun provideCommonInteractor(
        dAppMetadataRepository: DAppMetadataRepository,
        favouritesDAppRepository: FavouritesDAppRepository,
        phishingSitesRepository: PhishingSitesRepository,
        resourceManager: ResourceManager
    ) = DappInteractor(
        dAppMetadataRepository = dAppMetadataRepository,
        favouritesDAppRepository = favouritesDAppRepository,
        phishingSitesRepository = phishingSitesRepository,
        resourceManager = resourceManager
    )
}
