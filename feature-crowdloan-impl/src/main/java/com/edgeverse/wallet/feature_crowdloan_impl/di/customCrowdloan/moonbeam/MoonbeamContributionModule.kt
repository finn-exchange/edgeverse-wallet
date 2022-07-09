package com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.moonbeam

import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.moonbeam.MoonbeamApi
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeFactory
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamPrivateSignatureProvider
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.MoonbeamCrowdloanSubmitter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.MoonbeamStartFlowInterceptor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.ConfirmContributeMoonbeamCustomization
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.MoonbeamMainFlowCustomViewStateFactory
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.SelectContributeMoonbeamCustomization
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module
class MoonbeamContributionModule {

    @Provides
    @FeatureScope
    fun provideMoonbeamApi(
        networkApiCreator: NetworkApiCreator,
    ) = networkApiCreator.create(MoonbeamApi::class.java)

    @Provides
    @FeatureScope
    fun provideMoonbeamInteractor(
        accountRepository: AccountRepository,
        extrinsicService: ExtrinsicService,
        moonbeamApi: MoonbeamApi,
        selectedAssetSharedState: CrowdloanSharedState,
        httpExceptionHandler: HttpExceptionHandler,
        secretStoreV2: SecretStoreV2,
        chainRegistry: ChainRegistry,
    ) = MoonbeamCrowdloanInteractor(
        accountRepository,
        extrinsicService,
        moonbeamApi,
        selectedAssetSharedState,
        chainRegistry,
        httpExceptionHandler,
        secretStoreV2
    )

    @Provides
    @FeatureScope
    fun provideMoonbeamSubmitter(interactor: MoonbeamCrowdloanInteractor) = MoonbeamCrowdloanSubmitter(interactor)

    @Provides
    @FeatureScope
    fun provideMoonbeamStartFlowInterceptor(
        router: CrowdloanRouter,
        resourceManager: ResourceManager,
        interactor: MoonbeamCrowdloanInteractor,
        customDialogDisplayer: CustomDialogDisplayer.Presentation,
    ) = MoonbeamStartFlowInterceptor(
        crowdloanRouter = router,
        resourceManager = resourceManager,
        moonbeamInteractor = interactor,
        customDialogDisplayer = customDialogDisplayer,
    )

    @Provides
    @FeatureScope
    fun provideMoonbeamPrivateSignatureProvider(
        moonbeamApi: MoonbeamApi,
        httpExceptionHandler: HttpExceptionHandler,
    ) = MoonbeamPrivateSignatureProvider(moonbeamApi, httpExceptionHandler)

    @Provides
    @FeatureScope
    fun provideSelectContributeMoonbeamViewStateFactory(
        interactor: MoonbeamCrowdloanInteractor,
        resourceManager: ResourceManager,
        iconGenerator: AddressIconGenerator,
    ) = MoonbeamMainFlowCustomViewStateFactory(interactor, resourceManager, iconGenerator)

    @Provides
    @FeatureScope
    fun provideSelectContributeMoonbeamCustomization(
        viewStateFactory: MoonbeamMainFlowCustomViewStateFactory,
        imageLoader: ImageLoader,
    ) = SelectContributeMoonbeamCustomization(viewStateFactory, imageLoader)

    @Provides
    @FeatureScope
    fun provideConfirmContributeMoonbeamCustomization(
        viewStateFactory: MoonbeamMainFlowCustomViewStateFactory,
        imageLoader: ImageLoader,
    ) = ConfirmContributeMoonbeamCustomization(viewStateFactory, imageLoader)

    @Provides
    @FeatureScope
    @IntoSet
    fun provideMoonbeamFactory(
        submitter: MoonbeamCrowdloanSubmitter,
        moonbeamStartFlowInterceptor: MoonbeamStartFlowInterceptor,
        privateSignatureProvider: MoonbeamPrivateSignatureProvider,
        selectContributeMoonbeamCustomization: SelectContributeMoonbeamCustomization,
        confirmContributeMoonbeamCustomization: ConfirmContributeMoonbeamCustomization,
    ): CustomContributeFactory = MoonbeamContributeFactory(
        submitter = submitter,
        startFlowInterceptor = moonbeamStartFlowInterceptor,
        privateCrowdloanSignatureProvider = privateSignatureProvider,
        selectContributeCustomization = selectContributeMoonbeamCustomization,
        confirmContributeCustomization = confirmContributeMoonbeamCustomization
    )
}
