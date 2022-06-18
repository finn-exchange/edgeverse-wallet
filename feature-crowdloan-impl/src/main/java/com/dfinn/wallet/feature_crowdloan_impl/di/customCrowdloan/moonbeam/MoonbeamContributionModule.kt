package com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.moonbeam

import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.mixin.api.CustomDialogDisplayer
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.data.network.api.moonbeam.MoonbeamApi
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeFactory
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamPrivateSignatureProvider
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.MoonbeamCrowdloanSubmitter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.MoonbeamStartFlowInterceptor
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.ConfirmContributeMoonbeamCustomization
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.MoonbeamMainFlowCustomViewStateFactory
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.SelectContributeMoonbeamCustomization
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

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
