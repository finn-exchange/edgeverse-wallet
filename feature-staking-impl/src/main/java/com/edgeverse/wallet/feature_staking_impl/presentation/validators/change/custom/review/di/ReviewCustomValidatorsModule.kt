package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.review.di

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
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.review.ReviewCustomValidatorsViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase

@Module(includes = [ViewModelModule::class])
class ReviewCustomValidatorsModule {

    @Provides
    @IntoMap
    @ViewModelKey(ReviewCustomValidatorsViewModel::class)
    fun provideViewModel(
        addressIconGenerator: AddressIconGenerator,
        stakingInteractor: StakingInteractor,
        resourceManager: ResourceManager,
        router: StakingRouter,
        setupStakingSharedState: SetupStakingSharedState,
        tokenUseCase: TokenUseCase,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return ReviewCustomValidatorsViewModel(
            router,
            addressIconGenerator,
            stakingInteractor,
            resourceManager,
            setupStakingSharedState,
            selectedAssetState,
            tokenUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ReviewCustomValidatorsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ReviewCustomValidatorsViewModel::class.java)
    }
}
