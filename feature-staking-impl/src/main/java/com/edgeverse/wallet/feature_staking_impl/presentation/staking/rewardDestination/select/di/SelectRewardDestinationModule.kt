package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rewardDestination.ChangeRewardDestinationInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationMixin
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.SelectRewardDestinationViewModel

@Module(includes = [ViewModelModule::class])
class SelectRewardDestinationModule {

    @Provides
    @IntoMap
    @ViewModelKey(SelectRewardDestinationViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        rewardCalculatorFactory: RewardCalculatorFactory,
        resourceManager: ResourceManager,
        changeRewardDestinationInteractor: ChangeRewardDestinationInteractor,
        validationSystem: RewardDestinationValidationSystem,
        validationExecutor: ValidationExecutor,
        rewardDestinationMixin: RewardDestinationMixin.Presentation,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
    ): ViewModel {
        return SelectRewardDestinationViewModel(
            router,
            interactor,
            rewardCalculatorFactory,
            resourceManager,
            changeRewardDestinationInteractor,
            validationSystem,
            validationExecutor,
            feeLoaderMixin,
            rewardDestinationMixin
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): SelectRewardDestinationViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SelectRewardDestinationViewModel::class.java)
    }
}
