package com.edgeverse.wallet.feature_staking_impl.di

import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.core_db.dao.AccountStakingDao
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.*
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.controller.AccountControllerBalanceUpdater
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.historical.HistoricalTotalValidatorRewardUpdater
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.historical.HistoricalUpdateMediator
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.historical.HistoricalValidatorRewardPointsUpdater
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.scope.AccountStakingScope
import com.edgeverse.wallet.feature_wallet_api.data.cache.AssetCache
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.updaters.SingleChainUpdateSystem
import dagger.Module
import dagger.Provides

@Module
class StakingUpdatersModule {

    @Provides
    @FeatureScope
    fun provideAccountStakingScope(
        accountRepository: AccountRepository,
        accountStakingDao: AccountStakingDao,
        sharedState: StakingSharedState,
    ) = AccountStakingScope(
        accountRepository,
        accountStakingDao,
        sharedState
    )

    @Provides
    @FeatureScope
    fun provideActiveEraUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = ActiveEraUpdater(
        sharedState,
        chainRegistry,
        storageCache
    )

    @Provides
    @FeatureScope
    fun provideElectedNominatorsUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        bulkRetriever: BulkRetriever,
        storageCache: StorageCache,
    ) = ValidatorExposureUpdater(
        bulkRetriever,
        sharedState,
        chainRegistry,
        storageCache
    )

    @Provides
    @FeatureScope
    fun provideTotalInsuranceUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = TotalIssuanceUpdater(
        sharedState,
        storageCache,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideCurrentEraUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = CurrentEraUpdater(
        sharedState,
        chainRegistry,
        storageCache
    )

    @Provides
    @FeatureScope
    fun provideStakingLedgerUpdater(
        stakingRepository: StakingRepository,
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        accountStakingDao: AccountStakingDao,
        assetCache: AssetCache,
        storageCache: StorageCache,
        accountUpdateScope: AccountUpdateScope,
    ): StakingLedgerUpdater {
        return StakingLedgerUpdater(
            stakingRepository,
            sharedState,
            chainRegistry,
            accountStakingDao,
            storageCache,
            assetCache,
            accountUpdateScope
        )
    }

    @Provides
    @FeatureScope
    fun provideAccountValidatorPrefsUpdater(
        storageCache: StorageCache,
        scope: AccountStakingScope,
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
    ) = AccountValidatorPrefsUpdater(
        scope,
        storageCache,
        sharedState,
        chainRegistry,
    )

    @Provides
    @FeatureScope
    fun provideAccountNominationsUpdater(
        storageCache: StorageCache,
        scope: AccountStakingScope,
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
    ) = AccountNominationsUpdater(
        scope,
        storageCache,
        sharedState,
        chainRegistry,
    )

    @Provides
    @FeatureScope
    fun provideAccountRewardDestinationUpdater(
        storageCache: StorageCache,
        scope: AccountStakingScope,
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
    ) = AccountRewardDestinationUpdater(
        scope,
        storageCache,
        sharedState,
        chainRegistry,
    )

    @Provides
    @FeatureScope
    fun provideHistoryDepthUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = HistoryDepthUpdater(
        sharedState,
        chainRegistry, storageCache
    )

    @Provides
    @FeatureScope
    fun provideHistoricalMediator(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        bulkRetriever: BulkRetriever,
        stakingRepository: StakingRepository,
        storageCache: StorageCache,
    ) = HistoricalUpdateMediator(
        historicalUpdaters = listOf(
            HistoricalTotalValidatorRewardUpdater(),
            HistoricalValidatorRewardPointsUpdater(),
        ),
        stakingSharedState = sharedState,
        chainRegistry = chainRegistry,
        bulkRetriever = bulkRetriever,
        stakingRepository = stakingRepository,
        storageCache = storageCache
    )

    @Provides
    @FeatureScope
    fun provideAccountControllerBalanceUpdater(
        assetCache: AssetCache,
        scope: AccountStakingScope,
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
    ) = AccountControllerBalanceUpdater(
        scope,
        sharedState,
        chainRegistry,
        assetCache
    )

    @Provides
    @FeatureScope
    fun provideMinBondUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = MinBondUpdater(
        sharedState,
        chainRegistry,
        storageCache
    )

    @Provides
    @FeatureScope
    fun provideMaxNominatorsUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = MaxNominatorsUpdater(
        storageCache,
        sharedState,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideCounterForNominatorsUpdater(
        sharedState: StakingSharedState,
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ) = CounterForNominatorsUpdater(
        sharedState,
        chainRegistry,
        storageCache
    )

    @Provides
    @FeatureScope
    fun provideStakingUpdaterSystem(
        activeEraUpdater: ActiveEraUpdater,
        validatorExposureUpdater: ValidatorExposureUpdater,
        totalIssuanceUpdater: TotalIssuanceUpdater,
        currentEraUpdater: CurrentEraUpdater,
        stakingLedgerUpdater: StakingLedgerUpdater,
        accountValidatorPrefsUpdater: AccountValidatorPrefsUpdater,
        accountNominationsUpdater: AccountNominationsUpdater,
        rewardDestinationUpdater: AccountRewardDestinationUpdater,
        historyDepthUpdater: HistoryDepthUpdater,
        historicalUpdateMediator: HistoricalUpdateMediator,
        accountControllerBalanceUpdater: AccountControllerBalanceUpdater,
        minBondUpdater: MinBondUpdater,
        maxNominatorsUpdater: MaxNominatorsUpdater,
        counterForNominatorsUpdater: CounterForNominatorsUpdater,

        chainRegistry: ChainRegistry,
        stakingSharedState: StakingSharedState
    ): UpdateSystem = SingleChainUpdateSystem(
        updaters = listOf(
            activeEraUpdater,
            validatorExposureUpdater,
            totalIssuanceUpdater,
            currentEraUpdater,
            stakingLedgerUpdater,
            accountValidatorPrefsUpdater,
            accountNominationsUpdater,
            rewardDestinationUpdater,
            historyDepthUpdater,
            historicalUpdateMediator,
            accountControllerBalanceUpdater,
            minBondUpdater,
            maxNominatorsUpdater,
            counterForNominatorsUpdater,
        ),
        chainRegistry = chainRegistry,
        singleAssetSharedState = stakingSharedState
    )
}
