package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contributions.ContributionsInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions.UserContributionsViewModel

@Module(includes = [ViewModelModule::class])
class UserContributionsModule {

    @Provides
    @IntoMap
    @ViewModelKey(UserContributionsViewModel::class)
    fun provideViewModel(
        interactor: ContributionsInteractor,
        iconGenerator: AddressIconGenerator,
        crowdloanSharedState: CrowdloanSharedState,
        resourceManager: ResourceManager,
        router: CrowdloanRouter,
    ): ViewModel {
        return UserContributionsViewModel(
            interactor,
            iconGenerator,
            crowdloanSharedState,
            resourceManager,
            router
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): UserContributionsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(UserContributionsViewModel::class.java)
    }
}
