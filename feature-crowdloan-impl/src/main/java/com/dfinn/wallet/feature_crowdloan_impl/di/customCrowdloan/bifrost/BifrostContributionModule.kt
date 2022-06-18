package com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.bifrost

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_crowdloan_impl.BuildConfig
import com.dfinn.wallet.feature_crowdloan_impl.data.network.api.bifrost.BifrostApi
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeFactory
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.bifrost.BifrostContributeInteractor
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.bifrost.BifrostContributeSubmitter

@Module
class BifrostContributionModule {

    @Provides
    @FeatureScope
    fun provideBifrostApi(networkApiCreator: NetworkApiCreator): BifrostApi {
        return networkApiCreator.create(BifrostApi::class.java, customBaseUrl = BifrostApi.BASE_URL)
    }

    @Provides
    @FeatureScope
    fun provideBifrostInteractor(
        bifrostApi: BifrostApi,
        httpExceptionHandler: HttpExceptionHandler,
    ) = BifrostContributeInteractor(BuildConfig.BIFROST_NOVA_REFERRAL, bifrostApi, httpExceptionHandler)

    @Provides
    @FeatureScope
    fun provideBifrostSubmitter(
        interactor: BifrostContributeInteractor,
    ) = BifrostContributeSubmitter(interactor)

    @Provides
    @FeatureScope
    fun provideBifrostExtraFlow(
        interactor: BifrostContributeInteractor,
        resourceManager: ResourceManager,
    ) = BifrostExtraBonusFlow(interactor, resourceManager)

    @Provides
    @FeatureScope
    @IntoSet
    fun provideBifrostFactory(
        submitter: BifrostContributeSubmitter,
        bifrostExtraBonusFlow: BifrostExtraBonusFlow,
    ): CustomContributeFactory = BifrostContributeFactory(
        submitter,
        bifrostExtraBonusFlow
    )
}
