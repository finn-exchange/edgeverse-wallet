package com.dfinn.wallet.feature_crowdloan_impl.presentation.main.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.mixin.MixinFactory
import com.dfinn.wallet.common.mixin.api.CustomDialogDisplayer
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.core.updater.UpdateSystem
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.statefull.StatefulCrowdloanMixin
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.statefull.StatefulCrowdloanProviderFactory
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.main.CrowdloanViewModel
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
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
