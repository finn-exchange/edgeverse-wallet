package com.dfinn.wallet.feature_crowdloan_impl.di

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.mixin.MixinFactory
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.data.network.api.parachain.ParachainMetadataApi
import com.dfinn.wallet.feature_crowdloan_impl.data.repository.CrowdloanRepositoryImpl
import com.dfinn.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeModule
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.CrowdloanContributeInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import com.dfinn.wallet.feature_wallet_api.domain.AssetUseCase
import com.dfinn.wallet.feature_wallet_api.domain.TokenUseCase
import com.dfinn.wallet.feature_wallet_api.domain.implementations.AssetUseCaseImpl
import com.dfinn.wallet.feature_wallet_api.domain.implementations.SharedStateTokenUseCase
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorFactory
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.create
import com.dfinn.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.repository.ChainStateRepository
import com.dfinn.wallet.runtime.storage.source.StorageDataSource
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
