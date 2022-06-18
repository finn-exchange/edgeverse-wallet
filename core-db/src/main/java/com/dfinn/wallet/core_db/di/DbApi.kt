package com.dfinn.wallet.core_db.di

import com.dfinn.wallet.core_db.AppDatabase
import com.dfinn.wallet.core_db.dao.*

interface DbApi {

    fun provideDatabase(): AppDatabase

    fun provideAccountDao(): AccountDao

    fun provideNodeDao(): NodeDao

    fun provideAssetDao(): AssetDao

    fun provideOperationDao(): OperationDao

    fun providePhishingAddressDao(): PhishingAddressDao

    fun storageDao(): StorageDao

    fun tokenDao(): TokenDao

    fun accountStakingDao(): AccountStakingDao

    fun stakingTotalRewardDao(): StakingTotalRewardDao

    fun chainDao(): ChainDao

    fun metaAccountDao(): MetaAccountDao

    fun dappAuthorizationDao(): DappAuthorizationDao

    fun nftDao(): NftDao

    val phishingSitesDao: PhishingSitesDao

    val favouritesDAppsDao: FavouriteDAppsDao
}
