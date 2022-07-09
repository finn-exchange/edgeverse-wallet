package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.search.di

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
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.validators.current.search.SearchCustomValidatorsInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.search.SearchCustomValidatorsViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase

@Module(includes = [ViewModelModule::class])
class SearchCustomValidatorsModule {

    @Provides
    @IntoMap
    @ViewModelKey(SearchCustomValidatorsViewModel::class)
    fun provideViewModel(
        addressIconGenerator: AddressIconGenerator,
        resourceManager: ResourceManager,
        router: StakingRouter,
        setupStakingSharedState: SetupStakingSharedState,
        searchCustomValidatorsInteractor: SearchCustomValidatorsInteractor,
        validatorRecommendatorFactory: ValidatorRecommendatorFactory,
        tokenUseCase: TokenUseCase,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return SearchCustomValidatorsViewModel(
            router,
            addressIconGenerator,
            searchCustomValidatorsInteractor,
            resourceManager,
            setupStakingSharedState,
            validatorRecommendatorFactory,
            selectedAssetState,
            tokenUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SearchCustomValidatorsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SearchCustomValidatorsViewModel::class.java)
    }
}
