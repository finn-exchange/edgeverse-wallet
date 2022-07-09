package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSettingsProviderFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.settings.CustomValidatorsSettingsViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase

@Module(includes = [ViewModelModule::class])
class CustomValidatorsSettingsModule {

    @Provides
    @IntoMap
    @ViewModelKey(CustomValidatorsSettingsViewModel::class)
    fun provideViewModel(
        recommendationSettingsProviderFactory: RecommendationSettingsProviderFactory,
        router: StakingRouter,
        tokenUseCase: TokenUseCase
    ): ViewModel {
        return CustomValidatorsSettingsViewModel(
            router,
            recommendationSettingsProviderFactory,
            tokenUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): CustomValidatorsSettingsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CustomValidatorsSettingsViewModel::class.java)
    }
}
