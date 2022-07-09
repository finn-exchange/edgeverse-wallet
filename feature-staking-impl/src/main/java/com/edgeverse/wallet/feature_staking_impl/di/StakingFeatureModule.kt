package com.edgeverse.wallet.feature_staking_impl.di

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.memory.ComputationalCache
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core_db.dao.AccountStakingDao
import com.edgeverse.wallet.core_db.dao.StakingTotalRewardDao
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_staking_api.domain.api.EraTimeCalculatorFactory
import com.edgeverse.wallet.feature_staking_api.domain.api.IdentityRepository
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.subquery.StakingApi
import com.edgeverse.wallet.feature_staking_impl.data.network.subquery.SubQueryValidatorSetFetcher
import com.edgeverse.wallet.feature_staking_impl.data.repository.*
import com.edgeverse.wallet.feature_staking_impl.data.repository.datasource.StakingRewardsDataSource
import com.edgeverse.wallet.feature_staking_impl.data.repository.datasource.StakingStoriesDataSource
import com.edgeverse.wallet.feature_staking_impl.data.repository.datasource.StakingStoriesDataSourceImpl
import com.edgeverse.wallet.feature_staking_impl.data.repository.datasource.SubqueryStakingRewardsDataSource
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.alerts.AlertsInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.payout.PayoutInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSettingsProviderFactory
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.setup.SetupStakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.bond.BondMoreInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.controller.ControllerInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rebond.RebondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.redeem.RedeemInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rewardDestination.ChangeRewardDestinationInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validators.ValidatorProvider
import com.edgeverse.wallet.feature_staking_impl.domain.validators.current.CurrentValidatorsInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validators.current.search.SearchCustomValidatorsInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationMixin
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationProvider
import com.edgeverse.wallet.feature_wallet_api.domain.AssetUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.implementations.AssetUseCaseImpl
import com.edgeverse.wallet.feature_wallet_api.domain.implementations.SharedStateTokenUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.create
import com.edgeverse.wallet.runtime.di.LOCAL_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class StakingFeatureModule {

    @Provides
    @FeatureScope
    fun provideAssetUseCase(
        walletRepository: WalletRepository,
        accountRepository: AccountRepository,
        sharedState: StakingSharedState,
    ): AssetUseCase = AssetUseCaseImpl(
        walletRepository,
        accountRepository,
        sharedState
    )

    @Provides
    fun provideAssetSelectorMixinFactory(
        assetUseCase: AssetUseCase,
        singleAssetSharedState: StakingSharedState,
        resourceManager: ResourceManager
    ): MixinFactory<AssetSelectorMixin.Presentation> = AssetSelectorFactory(
        assetUseCase,
        singleAssetSharedState,
        resourceManager
    )

    @Provides
    @FeatureScope
    fun provideTokenUseCase(
        tokenRepository: TokenRepository,
        sharedState: StakingSharedState,
    ): TokenUseCase = SharedStateTokenUseCase(
        tokenRepository,
        sharedState
    )

    @Provides
    @FeatureScope
    fun provideFeeLoaderMixin(
        feeLoaderMixinFactory: FeeLoaderMixin.Factory,
        tokenUseCase: TokenUseCase,
    ): FeeLoaderMixin.Presentation = feeLoaderMixinFactory.create(tokenUseCase)

    @Provides
    @FeatureScope
    fun provideStakingSharedState(
        chainRegistry: ChainRegistry,
        preferences: Preferences
    ) = StakingSharedState(chainRegistry, preferences)

    @Provides
    @FeatureScope
    fun provideStakingStoriesDataSource(): StakingStoriesDataSource = StakingStoriesDataSourceImpl()

    @Provides
    @FeatureScope
    fun provideStakingRewardsSubqueryDataSource(
        stakingApi: StakingApi,
        stakingTotalRewardDao: StakingTotalRewardDao,
    ): StakingRewardsDataSource = SubqueryStakingRewardsDataSource(
        stakingApi = stakingApi,
        stakingTotalRewardDao = stakingTotalRewardDao
    )

    @Provides
    @FeatureScope
    fun provideStakingRepository(
        accountStakingDao: AccountStakingDao,
        @Named(LOCAL_STORAGE_SOURCE) localStorageSource: StorageDataSource,
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource,
        stakingStoriesDataSource: StakingStoriesDataSource,
        walletConstants: WalletConstants,
        chainRegistry: ChainRegistry,
    ): StakingRepository = StakingRepositoryImpl(
        accountStakingDao = accountStakingDao,
        remoteStorage = remoteStorageSource,
        localStorage = localStorageSource,
        stakingStoriesDataSource = stakingStoriesDataSource,
        walletConstants = walletConstants,
        chainRegistry = chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideIdentityRepository(
        bulkRetriever: BulkRetriever,
        chainRegistry: ChainRegistry,
    ): IdentityRepository = IdentityRepositoryImpl(
        bulkRetriever,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideStakingInteractor(
        walletRepository: WalletRepository,
        accountRepository: AccountRepository,
        stakingRepository: StakingRepository,
        stakingRewardsRepository: StakingRewardsRepository,
        stakingConstantsRepository: StakingConstantsRepository,
        identityRepository: IdentityRepository,
        payoutRepository: PayoutRepository,
        stakingSharedState: StakingSharedState,
        assetUseCase: AssetUseCase,
        factory: EraTimeCalculatorFactory,
    ) = StakingInteractor(
        walletRepository,
        accountRepository,
        stakingRepository,
        stakingRewardsRepository,
        stakingConstantsRepository,
        identityRepository,
        stakingSharedState,
        payoutRepository,
        assetUseCase,
        factory
    )

    @Provides
    @FeatureScope
    fun provideEraTimeCalculatorFactory(
        stakingRepository: StakingRepository,
    ) = EraTimeCalculatorFactory(stakingRepository)

    @Provides
    @FeatureScope
    fun provideAlertsInteractor(
        stakingRepository: StakingRepository,
        stakingConstantsRepository: StakingConstantsRepository,
        sharedState: StakingSharedState,
        walletRepository: WalletRepository,
    ) = AlertsInteractor(
        stakingRepository,
        stakingConstantsRepository,
        sharedState,
        walletRepository
    )

    @Provides
    @FeatureScope
    fun provideRewardCalculatorFactory(
        repository: StakingRepository,
        sharedState: StakingSharedState
    ) = RewardCalculatorFactory(repository, sharedState)

    @Provides
    @FeatureScope
    fun provideValidatorRecommendatorFactory(
        validatorProvider: ValidatorProvider,
        computationalCache: ComputationalCache,
        sharedState: StakingSharedState,
    ) = ValidatorRecommendatorFactory(validatorProvider, sharedState, computationalCache)

    @Provides
    @FeatureScope
    fun provideValidatorProvider(
        stakingRepository: StakingRepository,
        identityRepository: IdentityRepository,
        rewardCalculatorFactory: RewardCalculatorFactory,
        stakingConstantsRepository: StakingConstantsRepository,
    ) = ValidatorProvider(
        stakingRepository,
        identityRepository,
        rewardCalculatorFactory,
        stakingConstantsRepository
    )

    @Provides
    @FeatureScope
    fun provideStakingConstantsRepository(
        chainRegistry: ChainRegistry,
    ) = StakingConstantsRepository(chainRegistry)

    @Provides
    @FeatureScope
    fun provideRecommendationSettingsProviderFactory(
        stakingConstantsRepository: StakingConstantsRepository,
        computationalCache: ComputationalCache,
        sharedState: StakingSharedState,
    ) = RecommendationSettingsProviderFactory(
        computationalCache,
        stakingConstantsRepository,
        sharedState
    )

    @Provides
    @FeatureScope
    fun provideSetupStakingInteractor(
        extrinsicService: ExtrinsicService,
        sharedState: StakingSharedState,
    ) = SetupStakingInteractor(extrinsicService, sharedState)

    @Provides
    @FeatureScope
    fun provideSetupStakingSharedState() = SetupStakingSharedState()

    @Provides
    fun provideRewardDestinationChooserMixin(
        resourceManager: ResourceManager,
        appLinksProvider: AppLinksProvider,
        stakingInteractor: StakingInteractor,
        iconGenerator: AddressIconGenerator,
        accountDisplayUseCase: AddressDisplayUseCase,
        sharedState: StakingSharedState,
    ): RewardDestinationMixin.Presentation = RewardDestinationProvider(
        resourceManager, stakingInteractor, iconGenerator, appLinksProvider, sharedState, accountDisplayUseCase
    )

    @Provides
    @FeatureScope
    fun provideStakingRewardsApi(networkApiCreator: NetworkApiCreator): StakingApi {
        return networkApiCreator.create(StakingApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideStakingRewardsRepository(
        rewardDataSource: StakingRewardsDataSource,
    ): StakingRewardsRepository {
        return StakingRewardsRepository(rewardDataSource)
    }

    @Provides
    @FeatureScope
    fun provideValidatorSetFetcher(
        stakingApi: StakingApi,
        stakingRepository: StakingRepository,
    ): SubQueryValidatorSetFetcher {
        return SubQueryValidatorSetFetcher(
            stakingApi,
            stakingRepository
        )
    }

    @Provides
    @FeatureScope
    fun providePayoutRepository(
        stakingRepository: StakingRepository,
        validatorSetFetcher: SubQueryValidatorSetFetcher,
        bulkRetriever: BulkRetriever,
        storageCache: StorageCache,
        chainRegistry: ChainRegistry,
    ): PayoutRepository {
        return PayoutRepository(stakingRepository, bulkRetriever, validatorSetFetcher, storageCache, chainRegistry)
    }

    @Provides
    @FeatureScope
    fun providePayoutInteractor(
        sharedState: StakingSharedState,
        extrinsicService: ExtrinsicService,
    ) = PayoutInteractor(sharedState, extrinsicService)

    @Provides
    @FeatureScope
    fun provideBondMoreInteractor(
        sharedState: StakingSharedState,
        extrinsicService: ExtrinsicService,
    ) = BondMoreInteractor(extrinsicService, sharedState)

    @Provides
    @FeatureScope
    fun provideRedeemInteractor(
        extrinsicService: ExtrinsicService,
        stakingRepository: StakingRepository,
    ) = RedeemInteractor(extrinsicService, stakingRepository)

    @Provides
    @FeatureScope
    fun provideRebondInteractor(
        sharedState: StakingSharedState,
        extrinsicService: ExtrinsicService,
    ) = RebondInteractor(extrinsicService, sharedState)

    @Provides
    @FeatureScope
    fun provideControllerInteractor(
        sharedState: StakingSharedState,
        extrinsicService: ExtrinsicService,
    ) = ControllerInteractor(extrinsicService, sharedState)

    @Provides
    @FeatureScope
    fun provideCurrentValidatorsInteractor(
        stakingRepository: StakingRepository,
        stakingConstantsRepository: StakingConstantsRepository,
        validatorProvider: ValidatorProvider,
    ) = CurrentValidatorsInteractor(
        stakingRepository, stakingConstantsRepository, validatorProvider
    )

    @Provides
    @FeatureScope
    fun provideChangeRewardDestinationInteractor(
        extrinsicService: ExtrinsicService,
    ) = ChangeRewardDestinationInteractor(extrinsicService)

    @Provides
    @FeatureScope
    fun provideSearchCustomValidatorsInteractor(
        validatorProvider: ValidatorProvider,
        sharedState: StakingSharedState
    ) = SearchCustomValidatorsInteractor(validatorProvider, sharedState)

    @Provides
    @FeatureScope
    fun provideStakingHintsUseCase(
        resourceManager: ResourceManager,
        stakingInteractor: StakingInteractor
    ) = StakingHintsUseCase(resourceManager, stakingInteractor)
}
