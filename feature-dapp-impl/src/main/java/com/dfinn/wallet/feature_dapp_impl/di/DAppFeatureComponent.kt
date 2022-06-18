package com.dfinn.wallet.feature_dapp_impl.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_dapp_api.di.DAppFeatureApi
import com.dfinn.wallet.feature_dapp_impl.DAppRouter
import com.dfinn.wallet.feature_dapp_impl.presentation.addToFavourites.di.AddToFavouritesComponent
import com.dfinn.wallet.feature_dapp_impl.presentation.authorizedDApps.di.AuthorizedDAppsComponent
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.di.DAppExtrinsicDetailsComponent
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.main.di.DAppBrowserComponent
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.di.DAppSignComponent
import com.dfinn.wallet.feature_dapp_impl.presentation.main.di.MainDAppComponent
import com.dfinn.wallet.feature_dapp_impl.presentation.main.view.CategorizedDappsView
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator
import com.dfinn.wallet.feature_dapp_impl.presentation.search.di.DAppSearchComponent
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        DAppFeatureDependencies::class
    ],
    modules = [
        DappFeatureModule::class
    ]
)
@FeatureScope
interface DAppFeatureComponent : DAppFeatureApi {

    // Screens

    fun mainComponentFactory(): MainDAppComponent.Factory

    fun browserComponentFactory(): DAppBrowserComponent.Factory

    fun signExtrinsicComponentFactory(): DAppSignComponent.Factory

    fun extrinsicDetailsComponentFactory(): DAppExtrinsicDetailsComponent.Factory

    fun dAppSearchComponentFactory(): DAppSearchComponent.Factory

    fun addToFavouritesComponentFactory(): AddToFavouritesComponent.Factory

    fun authorizedDAppsComponentFactory(): AuthorizedDAppsComponent.Factory

    // Views

    fun inject(view: CategorizedDappsView)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance router: DAppRouter,
            @BindsInstance signCommunicator: DAppSignCommunicator,
            @BindsInstance searchCommunicator: DAppSearchCommunicator,
            deps: DAppFeatureDependencies
        ): DAppFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            AccountFeatureApi::class,
            WalletFeatureApi::class,
            RuntimeApi::class
        ]
    )
    interface DAppFeatureDependenciesComponent : DAppFeatureDependencies
}
