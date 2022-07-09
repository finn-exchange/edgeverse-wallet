package com.edgeverse.wallet.feature_crowdloan_impl.di

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parachain.ParachainMetadataApi
import com.edgeverse.wallet.feature_crowdloan_impl.data.repository.CrowdloanRepositoryImpl
import com.edgeverse.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeModule
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.CrowdloanContributeInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import com.edgeverse.wallet.feature_wallet_api.domain.AssetUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.implementations.AssetUseCaseImpl
import com.edgeverse.wallet.feature_wallet_api.domain.implementations.SharedStateTokenUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.create
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.repository.ChainStateRepository
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import javax.inject.Named

@Module(
    includes = [
        CustomContributeModule::class
    ]
)
class CrowdloanFeatureModule {

    @Provides
    @FeatureScope
    fun provideAssetUseCase(
        walletRepository: WalletRepository,
        accountRepository: AccountRepository,
        sharedState: CrowdloanSharedState,
    ): AssetUseCase = AssetUseCaseImpl(
        walletRepository,
        accountRepository,
        sharedState
    )

    @Provides
    fun provideAssetSelectorMixinFactory(
        assetUseCase: AssetUseCase,
        singleAssetSharedState: CrowdloanSharedState,
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
        sharedState: CrowdloanSharedState,
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
    fun provideCrowdloanSharedState(
        chainRegistry: ChainRegistry,
        preferences: Preferences,
    ) = CrowdloanSharedState(chainRegistry, preferences)

    @Provides
    @FeatureScope
    fun crowdloanRepository(
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource,
        crowdloanMetadataApi: ParachainMetadataApi,
        chainRegistry: ChainRegistry,
    ): CrowdloanRepository = CrowdloanRepositoryImpl(
        remoteStorageSource,
        chainRegistry,
        crowdloanMetadataApi
    )

    @Provides
    @FeatureScope
    fun provideCrowdloanInteractor(
        accountRepository: AccountRepository,
        crowdloanRepository: CrowdloanRepository,
        chainStateRepository: ChainStateRepository,
        externalContributionSource: ExternalContributionSource,
    ) = CrowdloanInteractor(
        accountRepository,
        crowdloanRepository,
        chainStateRepository,
        externalContributionSource
    )

    @Provides
    @FeatureScope
    fun provideCrowdloanMetadataApi(networkApiCreator: NetworkApiCreator): ParachainMetadataApi {
        return networkApiCreator.create(ParachainMetadataApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideCrowdloanContributeInteractor(
        extrinsicService: ExtrinsicService,
        accountRepository: AccountRepository,
        chainStateRepository: ChainStateRepository,
        sharedState: CrowdloanSharedState,
        crowdloanRepository: CrowdloanRepository,
        customContributeManager: CustomContributeManager,
    ) = CrowdloanContributeInteractor(
        extrinsicService,
        accountRepository,
        chainStateRepository,
        customContributeManager,
        sharedState,
        crowdloanRepository
    )
}
