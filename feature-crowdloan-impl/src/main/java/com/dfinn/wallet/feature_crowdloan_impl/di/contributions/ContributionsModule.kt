package com.dfinn.wallet.feature_crowdloan_impl.di.contributions

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.data.network.api.acala.AcalaApi
import com.dfinn.wallet.feature_crowdloan_impl.data.network.api.parallel.ParallelApi
import com.dfinn.wallet.feature_crowdloan_impl.data.repository.contributions.source.CompositeContributionsSource
import com.dfinn.wallet.feature_crowdloan_impl.data.repository.contributions.source.LiquidAcalaContributionSource
import com.dfinn.wallet.feature_crowdloan_impl.data.repository.contributions.source.ParallelContributionSource
import com.dfinn.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.dfinn.wallet.feature_crowdloan_impl.domain.contributions.ContributionsInteractor

@Module
class ContributionsModule {

    @Provides
    @FeatureScope
    @IntoSet
    fun acalaLiquidSource(
        acalaApi: AcalaApi,
    ): ExternalContributionSource = LiquidAcalaContributionSource(acalaApi)

    @Provides
    @FeatureScope
    @IntoSet
    fun parallelSource(
        parallelApi: ParallelApi,
    ): ExternalContributionSource = ParallelContributionSource(parallelApi)

    @Provides
    @FeatureScope
    fun compositeSource(
        childSources: Set<@JvmSuppressWildcards ExternalContributionSource>,
    ): ExternalContributionSource = CompositeContributionsSource(childSources)

    @Provides
    @FeatureScope
    fun provideContributionsInteractor(
        externalContributionsSource: ExternalContributionSource,
        crowdloanRepository: CrowdloanRepository,
        accountRepository: AccountRepository,
        crowdloanSharedState: CrowdloanSharedState,
    ) = ContributionsInteractor(
        externalContributionSource = externalContributionsSource,
        crowdloanRepository = crowdloanRepository,
        accountRepository = accountRepository,
        selectedAssetState = crowdloanSharedState
    )
}
