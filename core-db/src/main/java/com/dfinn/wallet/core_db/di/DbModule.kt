package com.dfinn.wallet.core_db.di

import android.content.Context
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.core_db.AppDatabase
import com.dfinn.wallet.core_db.dao.*
import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Provides
    @ApplicationScope
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return AppDatabase.get(context)
    }

    @Provides
    @ApplicationScope
    fun provideUserDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.userDao()
    }

    @Provides
    @ApplicationScope
    fun provideNodeDao(appDatabase: AppDatabase): NodeDao {
        return appDatabase.nodeDao()
    }

    @Provides
    @ApplicationScope
    fun provideAssetDao(appDatabase: AppDatabase): AssetDao {
        return appDatabase.assetDao()
    }

    @Provides
    @ApplicationScope
    fun provideOperationHistoryDao(appDatabase: AppDatabase): OperationDao {
        return appDatabase.operationDao()
    }

    @Provides
    @ApplicationScope
    fun providePhishingAddressDao(appDatabase: AppDatabase): PhishingAddressDao {
        return appDatabase.phishingAddressesDao()
    }

    @Provides
    @ApplicationScope
    fun provideStorageDao(appDatabase: AppDatabase): StorageDao {
        return appDatabase.storageDao()
    }

    @Provides
    @ApplicationScope
    fun provideTokenDao(appDatabase: AppDatabase): TokenDao {
        return appDatabase.tokenDao()
    }

    @Provides
    @ApplicationScope
    fun provideAccountStakingDao(appDatabase: AppDatabase): AccountStakingDao {
        return appDatabase.accountStakingDao()
    }

    @Provides
    @ApplicationScope
    fun provideStakingTotalRewardDao(appDatabase: AppDatabase): StakingTotalRewardDao {
        return appDatabase.stakingTotalRewardDao()
    }

    @Provides
    @ApplicationScope
    fun provideChainDao(appDatabase: AppDatabase): ChainDao {
        return appDatabase.chainDao()
    }

    @Provides
    @ApplicationScope
    fun provideMetaAccountDao(appDatabase: AppDatabase): MetaAccountDao {
        return appDatabase.metaAccountDao()
    }

    @Provides
    @ApplicationScope
    fun provideDappAuthorizationDao(appDatabase: AppDatabase): DappAuthorizationDao {
        return appDatabase.dAppAuthorizationDao()
    }

    @Provides
    @ApplicationScope
    fun provideNftDao(appDatabase: AppDatabase): NftDao {
        return appDatabase.nftDao()
    }

    @Provides
    @ApplicationScope
    fun providePhishingSitesDao(appDatabase: AppDatabase): PhishingSitesDao {
        return appDatabase.phishingSitesDao()
    }

    @Provides
    @ApplicationScope
    fun provideFavouriteDappsDao(appDatabase: AppDatabase): FavouriteDAppsDao {
        return appDatabase.favouriteDAppsDao()
    }
}
