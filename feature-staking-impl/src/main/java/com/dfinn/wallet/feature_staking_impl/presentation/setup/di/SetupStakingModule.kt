package com.dfinn.wallet.feature_staking_impl.presentation.setup.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.validation.ValidationSystem
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.dfinn.wallet.feature_staking_impl.domain.setup.SetupStakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.dfinn.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.dfinn.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationMixin
import com.dfinn.wallet.feature_staking_impl.presentation.setup.SetupStakingViewModel
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class SetupStakingModule {

    @Provides
    @IntoMap
    @ViewModelKey(SetupStakingViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        rewardCalculatorFactory: RewardCalculatorFactory,
        resourceManager: ResourceManager,
        setupStakingInteractor: SetupStakingInteractor,
        validationSystem: ValidationSystem<SetupStakingPayload, SetupStakingValidationFailure>,
        validationExecutor: ValidationExecutor,
        setupStakingSharedState: SetupStakingSharedState,
        rewardDestinationMixin: RewardDestinationMixin.Presentation,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        amountChooserMixinFactory: AmountChooserMixin.Factory
    ): ViewModel {
        return SetupStakingViewModel(
            router,
            interactor,
            rewardCalculatorFactory,
            resourceManager,
            setupStakingInteractor,
            validationSystem,
            setupStakingSharedState,
            validationExecutor,
            feeLoaderMixin,
            rewardDestinationMixin,
            amountChooserMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SetupStakingViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SetupStakingViewModel::class.java)
    }
}
