package com.edgeverse.wallet.core_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edgeverse.wallet.core_db.converters.*
import com.edgeverse.wallet.core_db.dao.*
import com.edgeverse.wallet.core_db.migrations.*
import com.edgeverse.wallet.core_db.model.*
import com.edgeverse.wallet.core_db.model.chain.*

@Database(
    version = 13,
    entities = [
        AccountLocal::class,
        NodeLocal::class,
        AssetLocal::class,
        TokenLocal::class,
        PhishingAddressLocal::class,
        StorageEntryLocal::class,
        AccountStakingLocal::class,
        TotalRewardLocal::class,
        OperationLocal::class,

        ChainLocal::class,
        ChainNodeLocal::class,
        ChainAssetLocal::class,
        ChainRuntimeInfoLocal::class,
        ChainExplorerLocal::class,
        MetaAccountLocal::class,
        ChainAccountLocal::class,

        DappAuthorizationLocal::class,
        NftLocal::class,

        PhishingSiteLocal::class,

        FavouriteDAppLocal::class
    ],
)
@TypeConverters(
    LongMathConverters::class,
    NetworkTypeConverters::class,
    TokenConverters::class,
    OperationConverters::class,
    CryptoTypeConverters::class,
    NftTypeConverters::class
)

abstract class AppDatabase : RoomDatabase() {

    companion object {

        private var instance: AppDatabase? = null

        @Synchronized
        fun get(
            context: Context
        ): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app.db"
                )
                    .addMigrations(AddDAppAuthorizations_1_2, AssetTypes_2_3, ChangeAsset_3_4)
                    .addMigrations(AddChainColor_4_5, AddNfts_5_6, AddSitePhishing_6_7, AddBuyProviders_7_8, BetterChainDiffing_8_9)
                    .addMigrations(AddFavouriteDApps_9_10, ChangeDAppAuthorization_10_11, RemoveChainForeignKeyFromChainAccount_11_12)
                    .addMigrations(AddAdditionalFieldToChains_12_13)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun nodeDao(): NodeDao

    abstract fun userDao(): AccountDao

    abstract fun assetDao(): AssetDao

    abstract fun operationDao(): OperationDao

    abstract fun phishingAddressesDao(): PhishingAddressDao

    abstract fun storageDao(): StorageDao

    abstract fun tokenDao(): TokenDao

    abstract fun accountStakingDao(): AccountStakingDao

    abstract fun stakingTotalRewardDao(): StakingTotalRewardDao

    abstract fun chainDao(): ChainDao

    abstract fun metaAccountDao(): MetaAccountDao

    abstract fun dAppAuthorizationDao(): DappAuthorizationDao

    abstract fun nftDao(): NftDao

    abstract fun phishingSitesDao(): PhishingSitesDao

    abstract fun favouriteDAppsDao(): FavouriteDAppsDao
}
