package com.dfinn.wallet.feature_dapp_impl.di.modules

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.dao.FavouriteDAppsDao
import com.dfinn.wallet.feature_dapp_impl.data.repository.DbFavouritesDAppRepository
import com.dfinn.wallet.feature_dapp_impl.data.repository.FavouritesDAppRepository

@Module
class FavouritesDAppModule {

    @Provides
    @FeatureScope
    fun provideFavouritesDAppRepository(
        dao: FavouriteDAppsDao
    ): FavouritesDAppRepository = DbFavouritesDAppRepository(dao)
}
