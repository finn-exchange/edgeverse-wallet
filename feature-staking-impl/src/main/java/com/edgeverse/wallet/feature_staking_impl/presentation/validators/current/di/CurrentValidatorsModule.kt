package com.edgeverse.wallet.feature_staking_impl.presentation.validators.current.di

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
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validators.current.CurrentValidatorsInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.current.CurrentValidatorsViewModel

@Module(includes = [ViewModelModule::class])
class CurrentValidatorsModule {

    @Provides
    @IntoMap
    @ViewModelKey(CurrentValidatorsViewModel::class)
    fun provideViewModel(
        stakingInteractor: StakingInteractor,
        resourceManager: ResourceManager,
        iconGenerator: AddressIconGenerator,
        currentValidatorsInteractor: CurrentValidatorsInteractor,
        setupStakingSharedState: SetupStakingSharedState,
        router: StakingRouter,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return CurrentValidatorsViewModel(
            router,
            resourceManager,
            stakingInteractor,
            iconGenerator,
            currentValidatorsInteractor,
            setupStakingSharedState,
            selectedAssetState
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): CurrentValidatorsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CurrentValidatorsViewModel::class.java)
    }
}
