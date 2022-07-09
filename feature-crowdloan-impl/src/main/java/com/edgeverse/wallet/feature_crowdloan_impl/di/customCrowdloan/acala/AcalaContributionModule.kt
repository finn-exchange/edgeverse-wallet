package com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.acala

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.acala.AcalaApi
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeFactory
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.acala.AcalaContributeInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.bonus.AcalaContributeSubmitter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.confirm.AcalaConfirmContributeCustomization
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.confirm.AcalaConfirmContributeViewStateFactory
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.select.AcalaSelectContributeCustomization
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module
class AcalaContributionModule {

    @Provides
    @FeatureScope
    fun provideAcalaApi(
        networkApiCreator: NetworkApiCreator,
    ) = networkApiCreator.create(AcalaApi::class.java)

    @Provides
    @FeatureScope
    fun provideAcalaInteractor(
        acalaApi: AcalaApi,
        httpExceptionHandler: HttpExceptionHandler,
        secretStoreV2: SecretStoreV2,
        selectAssetSharedState: CrowdloanSharedState,
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
    ) = AcalaContributeInteractor(acalaApi, httpExceptionHandler, accountRepository, secretStoreV2, chainRegistry, selectAssetSharedState)

    @Provides
    @FeatureScope
    fun provideAcalaSubmitter(
        interactor: AcalaContributeInteractor,
    ) = AcalaContributeSubmitter(interactor)

    @Provides
    @FeatureScope
    fun provideAcalaExtraBonusFlow(
        acalaInteractor: AcalaContributeInteractor,
        resourceManager: ResourceManager,
    ): AcalaExtraBonusFlow = AcalaExtraBonusFlow(
        interactor = acalaInteractor,
        resourceManager = resourceManager,
    )

    @Provides
    @FeatureScope
    fun provideKaruraExtraBonusFlow(
        acalaInteractor: AcalaContributeInteractor,
        resourceManager: ResourceManager,
    ): KaruraExtraBonusFlow = KaruraExtraBonusFlow(
        interactor = acalaInteractor,
        resourceManager = resourceManager,
    )

    @Provides
    @FeatureScope
    fun provideAcalaSelectContributeCustomization(): AcalaSelectContributeCustomization = AcalaSelectContributeCustomization()

    @Provides
    @FeatureScope
    fun provideAcalaConfirmContributeViewStateFactory(
        resourceManager: ResourceManager,
    ) = AcalaConfirmContributeViewStateFactory(resourceManager)

    @Provides
    @FeatureScope
    fun provideAcalaConfirmContributeCustomization(
        viewStateFactory: AcalaConfirmContributeViewStateFactory,
    ): AcalaConfirmContributeCustomization = AcalaConfirmContributeCustomization(viewStateFactory)

    @Provides
    @FeatureScope
    @IntoSet
    fun provideAcalaFactory(
        submitter: AcalaContributeSubmitter,
        acalaExtraBonusFlow: AcalaExtraBonusFlow,
        acalaSelectContributeCustomization: AcalaSelectContributeCustomization,
        acalaConfirmContributeCustomization: AcalaConfirmContributeCustomization,
    ): CustomContributeFactory = AcalaContributeFactory(
        submitter = submitter,
        extraBonusFlow = acalaExtraBonusFlow,
        selectContributeCustomization = acalaSelectContributeCustomization,
        confirmContributeCustomization = acalaConfirmContributeCustomization
    )

    @Provides
    @FeatureScope
    @IntoSet
    fun provideKaruraFactory(
        submitter: AcalaContributeSubmitter,
        karuraExtraBonusFlow: KaruraExtraBonusFlow,
    ): CustomContributeFactory = KaruraContributeFactory(
        submitter = submitter,
        extraBonusFlow = karuraExtraBonusFlow
    )
}