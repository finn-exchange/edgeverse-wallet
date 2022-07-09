package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.PayoutsListViewModel

@Module(includes = [ViewModelModule::class])
class PayoutsListModule {

    @Provides
    @IntoMap
    @ViewModelKey(PayoutsListViewModel::class)
    fun provideViewModel(
        stakingInteractor: StakingInteractor,
        resourceManager: ResourceManager,
        router: StakingRouter,
    ): ViewModel {
        return PayoutsListViewModel(
            router = router,
            resourceManager = resourceManager,
            interactor = stakingInteractor,
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): PayoutsListViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(PayoutsListViewModel::class.java)
    }
}
