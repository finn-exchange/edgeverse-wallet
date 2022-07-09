package com.edgeverse.wallet.feature_crowdloan_impl.presentation.main.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.statefull.StatefulCrowdloanMixin
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.statefull.StatefulCrowdloanProviderFactory
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.main.CrowdloanViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
class CrowdloanModule {

    @Provides
    @ScreenScope
    fun provideCrowdloanMixinFactory(
        crowdloanSharedState: CrowdloanSharedState,
        interactor: CrowdloanInteractor,
    ): StatefulCrowdloanMixin.Factory {
        return StatefulCrowdloanProviderFactory(
            singleAssetSharedState = crowdloanSharedState,
            interactor = interactor
        )
    }

    @Provides
    @IntoMap
    @ViewModelKey(CrowdloanViewModel::class)
    fun provideViewModel(
        resourceManager: ResourceManager,
        iconGenerator: AddressIconGenerator,
        crowdloanSharedState: CrowdloanSharedState,
        router: CrowdloanRouter,
        crowdloanUpdateSystem: UpdateSystem,
        assetSelectorFactory: MixinFactory<AssetSelectorMixin.Presentation>,
        customDialogDisplayer: CustomDialogDisplayer.Presentation,
        customContributeManager: CustomContributeManager,
        statefulCrowdloanMixinFactory: StatefulCrowdloanMixin.Factory,
    ): ViewModel {
        return CrowdloanViewModel(
            iconGenerator,
            resourceManager,
            crowdloanSharedState,
            router,
            customContributeManager,
            crowdloanUpdateSystem,
            assetSelectorFactory,
            statefulCrowdloanMixinFactory,
            customDialogDisplayer
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): CrowdloanViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CrowdloanViewModel::class.java)
    }
}
