package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.di

import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.NominatorViewState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.StashNoneViewState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.ValidatorViewState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.WelcomeViewState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.manage.ManageStakeMixinFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding.UnbondingMixinFactory
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class StakingViewStateFactory(
    private val stakingInteractor: StakingInteractor,
    private val setupStakingSharedState: SetupStakingSharedState,
    private val resourceManager: ResourceManager,
    private val router: StakingRouter,
    private val rewardCalculatorFactory: RewardCalculatorFactory,
    private val welcomeStakingValidationSystem: WelcomeStakingValidationSystem,
    private val manageStakeMixinFactory: ManageStakeMixinFactory,
    private val unbondingMixinFactory: UnbondingMixinFactory,
    private val validationExecutor: ValidationExecutor
) {

    fun createValidatorViewState(
        stakingState: StakingState.Stash.Validator,
        currentAssetFlow: Flow<Asset>,
        scope: CoroutineScope,
        errorDisplayer: (Throwable) -> Unit
    ) = ValidatorViewState(
        validatorState = stakingState,
        stakingInteractor = stakingInteractor,
        currentAssetFlow = currentAssetFlow,
        scope = scope,
        router = router,
        errorDisplayer = errorDisplayer,
        resourceManager = resourceManager,
        validationExecutor = validationExecutor,
        unbondingMixinFactory = unbondingMixinFactory,
        manageStakeMixinFactory = manageStakeMixinFactory,
    )

    fun createStashNoneState(
        currentAssetFlow: Flow<Asset>,
        accountStakingState: StakingState.Stash.None,
        scope: CoroutineScope,
        errorDisplayer: (Throwable) -> Unit
    ) = StashNoneViewState(
        stashState = accountStakingState,
        currentAssetFlow = currentAssetFlow,
        stakingInteractor = stakingInteractor,
        resourceManager = resourceManager,
        scope = scope,
        router = router,
        errorDisplayer = errorDisplayer,
        validationExecutor = validationExecutor,
        unbondingMixinFactory = unbondingMixinFactory,
        manageStakeMixinFactory = manageStakeMixinFactory,
    )

    fun createWelcomeViewState(
        scope: CoroutineScope,
        errorDisplayer: (String) -> Unit,
        currentAssetFlow: Flow<Asset>,
    ) = WelcomeViewState(
        setupStakingSharedState = setupStakingSharedState,
        rewardCalculatorFactory = rewardCalculatorFactory,
        resourceManager = resourceManager,
        router = router,
        scope = scope,
        errorDisplayer = errorDisplayer,
        validationSystem = welcomeStakingValidationSystem,
        validationExecutor = validationExecutor,
        currentAssetFlow = currentAssetFlow,
    )

    fun createNominatorViewState(
        stakingState: StakingState.Stash.Nominator,
        currentAssetFlow: Flow<Asset>,
        scope: CoroutineScope,
        errorDisplayer: (Throwable) -> Unit
    ) = NominatorViewState(
        nominatorState = stakingState,
        stakingInteractor = stakingInteractor,
        currentAssetFlow = currentAssetFlow,
        scope = scope,
        router = router,
        errorDisplayer = errorDisplayer,
        resourceManager = resourceManager,
        validationExecutor = validationExecutor,
        unbondingMixinFactory = unbondingMixinFactory,
        manageStakeMixinFactory = manageStakeMixinFactory,
    )
}
