package com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations.di

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
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations.ConfirmNominationsViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase

@Module(includes = [ViewModelModule::class])
class ConfirmNominationsModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmNominationsViewModel::class)
    fun provideViewModel(
        addressIconGenerator: AddressIconGenerator,
        resourceManager: ResourceManager,
        router: StakingRouter,
        setupStakingSharedState: SetupStakingSharedState,
        tokenUseCase: TokenUseCase,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return ConfirmNominationsViewModel(
            router,
            addressIconGenerator,
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
    ): ConfirmNominationsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmNominationsViewModel::class.java)
    }
}
