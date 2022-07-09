package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.select.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.modules.Caching
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSettingsProviderFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.select.SelectCustomValidatorsViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase

@Module(includes = [ViewModelModule::class])
class SelectCustomValidatorsModule {

    @Provides
    @IntoMap
    @ViewModelKey(SelectCustomValidatorsViewModel::class)
    fun provideViewModel(
        validatorRecommendatorFactory: ValidatorRecommendatorFactory,
        recommendationSettingsProviderFactory: RecommendationSettingsProviderFactory,
        @Caching addressIconGenerator: AddressIconGenerator,
        stakingInteractor: StakingInteractor,
        resourceManager: ResourceManager,
        setupStakingSharedState: SetupStakingSharedState,
        router: StakingRouter,
        tokenUseCase: TokenUseCase,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return SelectCustomValidatorsViewModel(
            router,
            validatorRecommendatorFactory,
            recommendationSettingsProviderFactory,
            addressIconGenerator,
            stakingInteractor,
            resourceManager,
            setupStakingSharedState,
            tokenUseCase,
            selectedAssetState
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SelectCustomValidatorsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SelectCustomValidatorsViewModel::class.java)
    }
}
