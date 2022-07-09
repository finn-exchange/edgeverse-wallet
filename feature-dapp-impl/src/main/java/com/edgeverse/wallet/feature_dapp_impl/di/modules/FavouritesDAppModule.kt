package com.edgeverse.wallet.feature_dapp_impl.di.modules

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.dao.FavouriteDAppsDao
import com.edgeverse.wallet.feature_dapp_impl.data.repository.DbFavouritesDAppRepository
import com.edgeverse.wallet.feature_dapp_impl.data.repository.FavouritesDAppRepository

@Module
class FavouritesDAppModule {

    @Provides
    @FeatureScope
    fun provideFavouritesDAppRepository(
        dao: FavouriteDAppsDao
    ): FavouritesDAppRepository = DbFavouritesDAppRepository(dao)
}
