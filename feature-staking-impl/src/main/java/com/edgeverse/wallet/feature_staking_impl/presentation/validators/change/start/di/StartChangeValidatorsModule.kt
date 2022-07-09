package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.start.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.start.StartChangeValidatorsViewModel

@Module(includes = [ViewModelModule::class])
class StartChangeValidatorsModule {

    @Provides
    @IntoMap
    @ViewModelKey(StartChangeValidatorsViewModel::class)
    fun provideViewModel(
        validatorRecommendatorFactory: ValidatorRecommendatorFactory,
        router: StakingRouter,
        sharedState: SetupStakingSharedState,
        resourceManager: ResourceManager,
        appLinksProvider: AppLinksProvider,
        interactor: StakingInteractor
    ): ViewModel {
        return StartChangeValidatorsViewModel(
            router = router,
            validatorRecommendatorFactory = validatorRecommendatorFactory,
            setupStakingSharedState = sharedState,
            appLinksProvider = appLinksProvider,
            resourceManager = resourceManager,
            interactor = interactor
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): StartChangeValidatorsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(StartChangeValidatorsViewModel::class.java)
    }
}
