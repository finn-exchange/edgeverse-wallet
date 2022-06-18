package com.dfinn.wallet.feature_crowdloan_impl.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.dfinn.wallet.feature_crowdloan_impl.di.contributions.ContributionsModule
import com.dfinn.wallet.feature_crowdloan_impl.di.validations.CrowdloansValidationsModule
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm.di.ConfirmContributeComponent
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.di.CustomContributeComponent
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.di.MoonbeamCrowdloanTermsComponent
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.ReferralContributeView
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.di.CrowdloanContributeComponent
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contributions.di.UserContributionsComponent
import com.dfinn.wallet.feature_crowdloan_impl.presentation.main.di.CrowdloanComponent
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        CrowdloanFeatureDependencies::class
    ],
    modules = [
        CrowdloanFeatureModule::class,
        CrowdloanUpdatersModule::class,
        CrowdloansValidationsModule::class,
        ContributionsModule::class
    ]
)
@FeatureScope
interface CrowdloanFeatureComponent : CrowdloanFeatureApi {

    fun crowdloansFactory(): CrowdloanComponent.Factory

    fun userContributionsFactory(): UserContributionsComponent.Factory

    fun selectContributeFactory(): CrowdloanContributeComponent.Factory

    fun confirmContributeFactory(): ConfirmContributeComponent.Factory

    fun customContributeFactory(): CustomContributeComponent.Factory

    fun moonbeamTermsFactory(): MoonbeamCrowdloanTermsComponent.Factory

    fun inject(view: ReferralContributeView)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance router: CrowdloanRouter,
            deps: CrowdloanFeatureDependencies,
        ): CrowdloanFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            RuntimeApi::class,
            AccountFeatureApi::class,
            WalletFeatureApi::class
        ]
    )
    interface CrowdloanFeatureDependenciesComponent : CrowdloanFeatureDependencies
}
