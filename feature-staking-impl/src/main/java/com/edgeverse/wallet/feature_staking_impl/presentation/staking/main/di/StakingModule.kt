package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.alerts.AlertsInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.SYSTEM_MANAGE_STAKING_BOND_MORE
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.SYSTEM_MANAGE_STAKING_REBOND
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.SYSTEM_MANAGE_STAKING_REDEEM
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.StakeActionsValidationSystem
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.StakingViewModel
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.manage.ManageStakeAction
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.manage.ManageStakeMixinFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding.UnbondingMixinFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module(includes = [ViewModelModule::class])
class StakingModule {

    @Provides
    @ScreenScope
    fun provideUnbondingMixinFactory(
        unbondInteractor: UnbondInteractor,
        validationExecutor: ValidationExecutor,
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory,
        resourceManager: ResourceManager,
        @Named(SYSTEM_MANAGE_STAKING_REDEEM) redeemValidationSystem: StakeActionsValidationSystem,
        @Named(SYSTEM_MANAGE_STAKING_REBOND) rebondValidationSystem: StakeActionsValidationSystem,
        router: StakingRouter,
    ) = UnbondingMixinFactory(
        unbondInteractor = unbondInteractor,
        validationExecutor = validationExecutor,
        actionAwaitableFactory = actionAwaitableMixinFactory,
        resourceManager = resourceManager,
        rebondValidationSystem = rebondValidationSystem,
        redeemValidationSystem = redeemValidationSystem,
        router = router
    )

    @Provides
    @ScreenScope
    fun provideManageStakingMixinFactory(
        validationExecutor: ValidationExecutor,
        resourceManager: ResourceManager,
        router: StakingRouter,
        stakeActionsValidations: Map<@JvmSuppressWildcards ManageStakeAction, StakeActionsValidationSystem>,
    ) = ManageStakeMixinFactory(
        validationExecutor = validationExecutor,
        resourceManager = resourceManager,
        stakeActionsValidations = stakeActionsValidations,
        router = router
    )

    @Provides
    @ScreenScope
    fun provideStakingViewStateFactory(
        interactor: StakingInteractor,
        setupStakingSharedState: SetupStakingSharedState,
        resourceManager: ResourceManager,
        rewardCalculatorFactory: RewardCalculatorFactory,
        router: StakingRouter,
        welcomeStakingValidationSystem: WelcomeStakingValidationSystem,
        validationExecutor: ValidationExecutor,
        unbondingMixinFactory: UnbondingMixinFactory,
        manageStakeMixinFactory: ManageStakeMixinFactory,
    ) = StakingViewStateFactory(
        stakingInteractor = interactor,
        setupStakingSharedState = setupStakingSharedState,
        resourceManager = resourceManager,
        router = router,
        rewardCalculatorFactory = rewardCalculatorFactory,
        welcomeStakingValidationSystem = welcomeStakingValidationSystem,
        manageStakeMixinFactory = manageStakeMixinFactory,
        unbondingMixinFactory = unbondingMixinFactory,
        validationExecutor = validationExecutor
    )

    @Provides
    @IntoMap
    @ViewModelKey(StakingViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        alertsInteractor: AlertsInteractor,
        addressIconGenerator: AddressIconGenerator,
        stakingViewStateFactory: StakingViewStateFactory,
        router: StakingRouter,
        resourceManager: ResourceManager,
        @Named(SYSTEM_MANAGE_STAKING_REDEEM) redeemValidationSystem: StakeActionsValidationSystem,
        @Named(SYSTEM_MANAGE_STAKING_BOND_MORE) bondMoreValidationSystem: StakeActionsValidationSystem,
        validationExecutor: ValidationExecutor,
        stakingUpdateSystem: UpdateSystem,
        assetSelectorFactory: MixinFactory<AssetSelectorMixin.Presentation>,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return StakingViewModel(
            interactor = interactor,
            alertsInteractor = alertsInteractor,
            addressIconGenerator = addressIconGenerator,
            stakingViewStateFactory = stakingViewStateFactory,
            router = router,
            resourceManager = resourceManager,
            redeemValidationSystem = redeemValidationSystem,
            bondMoreValidationSystem = bondMoreValidationSystem,
            validationExecutor = validationExecutor,
            stakingUpdateSystem = stakingUpdateSystem,
            assetSelectorMixinFactory = assetSelectorFactory,
            selectedAssetState = selectedAssetState,
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): StakingViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(StakingViewModel::class.java)
    }
}
