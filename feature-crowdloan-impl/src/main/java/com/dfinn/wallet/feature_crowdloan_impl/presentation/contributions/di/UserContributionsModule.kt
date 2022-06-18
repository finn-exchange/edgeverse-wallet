package com.dfinn.wallet.feature_crowdloan_impl.presentation.contributions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.domain.contributions.ContributionsInteractor
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contributions.UserContributionsViewModel

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
