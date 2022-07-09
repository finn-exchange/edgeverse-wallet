package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.TitleAndMessage
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.WithCoroutineScopeExtensions
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.formatFractionAsPercentage
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.withLoading
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.model.NominatorStatus
import com.edgeverse.wallet.feature_staking_impl.domain.model.NominatorStatus.Inactive.Reason
import com.edgeverse.wallet.feature_staking_impl.domain.model.StakeSummary
import com.edgeverse.wallet.feature_staking_impl.domain.model.StashNoneStatus
import com.edgeverse.wallet.feature_staking_impl.domain.model.ValidatorStatus
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculator
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.calculateMaxPeriodReturns
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.maxCompoundAPY
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.manage.ManageStakeMixinFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding.UnbondingMixinFactory
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountModel
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed class StakingViewState(
    coroutineScope: CoroutineScope,
    validationExecutor: ValidationExecutor
) :
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope),
    Validatable by validationExecutor

private const val PERIOD_MONTH = 30

class ReturnsModel(
    val monthlyPercentage: String,
    val yearlyPercentage: String,
)

class StakeSummaryModel<S>(
    val status: S,
    val totalStaked: AmountModel,
)

typealias NominatorSummaryModel = StakeSummaryModel<NominatorStatus>
typealias ValidatorSummaryModel = StakeSummaryModel<ValidatorStatus>
typealias StashNoneSummaryModel = StakeSummaryModel<StashNoneStatus>

@Suppress("LeakingThis")
sealed class StakeViewState<S>(
    private val stakeState: StakingState.Stash,
    protected val currentAssetFlow: Flow<Asset>,
    protected val stakingInteractor: StakingInteractor,
    protected val resourceManager: ResourceManager,
    protected val scope: CoroutineScope,
    protected val router: StakingRouter,
    protected val summaryFlowProvider: suspend (StakingState.Stash) -> Flow<StakeSummary<S>>,
    protected val statusMessageProvider: (S) -> TitleAndMessage,
    errorDisplayer: (Throwable) -> Unit,
    validationExecutor: ValidationExecutor,
    unbondingMixinFactory: UnbondingMixinFactory,
    manageStakeMixinFactory: ManageStakeMixinFactory,
) : StakingViewState(scope, validationExecutor) {

    val unbondingMixin = unbondingMixinFactory.create(
        errorDisplayer = errorDisplayer,
        stashState = stakeState,
        assetFlow = currentAssetFlow,
        coroutineScope = coroutineScope
    )

    val manageStakeMixin = manageStakeMixinFactory.create(
        errorDisplayer = errorDisplayer,
        stashState = stakeState,
        coroutineScope = coroutineScope
    )

    init {
        syncStakingRewards()
    }

    val userRewardsFlow = combine(
        stakingInteractor.observeUserRewards(stakeState),
        currentAssetFlow
    ) { totalRewards, currentAsset ->
        mapAmountToAmountModel(totalRewards, currentAsset)
    }
        .withLoading()
        .inBackground()
        .share()

    val stakeSummaryFlow = flow { emitAll(summaryFlow()) }
        .withLoading()
        .inBackground()
        .share()

    private val _showStatusAlertEvent = MutableLiveData<Event<Pair<String, String>>>()
    val showStatusAlertEvent: LiveData<Event<Pair<String, String>>> = _showStatusAlertEvent

    fun statusClicked() {
        val nominatorSummaryModel = loadedSummaryOrNull() ?: return

        val titleAndMessage = statusMessageProvider(nominatorSummaryModel.status)

        _showStatusAlertEvent.value = Event(titleAndMessage)
    }

    private fun syncStakingRewards() {
        scope.launch {
            stakingInteractor.syncStakingRewards(stakeState)
        }
    }

    private suspend fun summaryFlow(): Flow<StakeSummaryModel<S>> {
        return combine(
            summaryFlowProvider(stakeState),
            currentAssetFlow
        ) { summary, asset ->
            StakeSummaryModel(
                status = summary.status,
                totalStaked = mapAmountToAmountModel(summary.totalStaked, asset),
            )
        }
    }

    private fun loadedSummaryOrNull(): StakeSummaryModel<S>? {
        return when (val state = stakeSummaryFlow.replayCache.firstOrNull()) {
            is LoadingState.Loaded<StakeSummaryModel<S>> -> state.data
            else -> null
        }
    }
}

class ValidatorViewState(
    validatorState: StakingState.Stash.Validator,
    currentAssetFlow: Flow<Asset>,
    stakingInteractor: StakingInteractor,
    resourceManager: ResourceManager,
    scope: CoroutineScope,
    router: StakingRouter,
    errorDisplayer: (Throwable) -> Unit,
    validationExecutor: ValidationExecutor,
    unbondingMixinFactory: UnbondingMixinFactory,
    manageStakeMixinFactory: ManageStakeMixinFactory,
) : StakeViewState<ValidatorStatus>(
    stakeState = validatorState,
    currentAssetFlow = currentAssetFlow,
    stakingInteractor = stakingInteractor,
    resourceManager = resourceManager,
    scope = scope,
    router = router,
    errorDisplayer = errorDisplayer,
    summaryFlowProvider = { stakingInteractor.observeValidatorSummary(validatorState) },
    statusMessageProvider = { getValidatorStatusTitleAndMessage(resourceManager, it) },
    validationExecutor = validationExecutor,
    manageStakeMixinFactory = manageStakeMixinFactory,
    unbondingMixinFactory = unbondingMixinFactory
)

private fun getValidatorStatusTitleAndMessage(
    resourceManager: ResourceManager,
    status: ValidatorStatus
): Pair<String, String> {
    val (titleRes, messageRes) = when (status) {
        ValidatorStatus.ACTIVE -> R.string.staking_nominator_status_alert_active_title to R.string.staking_nominator_status_alert_active_message

        ValidatorStatus.INACTIVE -> R.string.staking_nominator_status_alert_inactive_title to R.string.staking_nominator_status_alert_no_validators
    }

    return resourceManager.getString(titleRes) to resourceManager.getString(messageRes)
}

class StashNoneViewState(
    stashState: StakingState.Stash.None,
    currentAssetFlow: Flow<Asset>,
    stakingInteractor: StakingInteractor,
    resourceManager: ResourceManager,
    scope: CoroutineScope,
    router: StakingRouter,
    errorDisplayer: (Throwable) -> Unit,
    validationExecutor: ValidationExecutor,
    unbondingMixinFactory: UnbondingMixinFactory,
    manageStakeMixinFactory: ManageStakeMixinFactory,
) : StakeViewState<StashNoneStatus>(
    stakeState = stashState,
    currentAssetFlow = currentAssetFlow,
    stakingInteractor = stakingInteractor,
    resourceManager = resourceManager,
    scope = scope,
    router = router,
    errorDisplayer = errorDisplayer,
    summaryFlowProvider = { stakingInteractor.observeStashSummary(stashState) },
    statusMessageProvider = { getStashStatusTitleAndMessage(resourceManager, it) },
    validationExecutor = validationExecutor,
    manageStakeMixinFactory = manageStakeMixinFactory,
    unbondingMixinFactory = unbondingMixinFactory
)

private fun getStashStatusTitleAndMessage(
    resourceManager: ResourceManager,
    status: StashNoneStatus
): Pair<String, String> {
    val (titleRes, messageRes) = when (status) {
        StashNoneStatus.INACTIVE -> R.string.staking_nominator_status_alert_inactive_title to R.string.staking_bonded_inactive
    }

    return resourceManager.getString(titleRes) to resourceManager.getString(messageRes)
}

class NominatorViewState(
    nominatorState: StakingState.Stash.Nominator,
    currentAssetFlow: Flow<Asset>,
    stakingInteractor: StakingInteractor,
    resourceManager: ResourceManager,
    scope: CoroutineScope,
    router: StakingRouter,
    errorDisplayer: (Throwable) -> Unit,
    validationExecutor: ValidationExecutor,
    unbondingMixinFactory: UnbondingMixinFactory,
    manageStakeMixinFactory: ManageStakeMixinFactory,
) : StakeViewState<NominatorStatus>(
    stakeState = nominatorState,
    currentAssetFlow = currentAssetFlow,
    stakingInteractor = stakingInteractor,
    resourceManager = resourceManager,
    scope = scope,
    router = router,
    errorDisplayer = errorDisplayer,
    summaryFlowProvider = { stakingInteractor.observeNominatorSummary(nominatorState) },
    statusMessageProvider = { getNominatorStatusTitleAndMessage(resourceManager, it) },
    validationExecutor = validationExecutor,
    manageStakeMixinFactory = manageStakeMixinFactory,
    unbondingMixinFactory = unbondingMixinFactory
)

private fun getNominatorStatusTitleAndMessage(
    resourceManager: ResourceManager,
    status: NominatorStatus
): Pair<String, String> {
    val (titleRes, messageRes) = when (status) {
        is NominatorStatus.Active -> R.string.staking_nominator_status_alert_active_title to R.string.staking_nominator_status_alert_active_message

        is NominatorStatus.Waiting -> R.string.staking_nominator_status_waiting to R.string.staking_nominator_status_alert_waiting_message

        is NominatorStatus.Inactive -> when (status.reason) {
            Reason.MIN_STAKE -> R.string.staking_nominator_status_alert_inactive_title to R.string.staking_nominator_status_alert_low_stake
            Reason.NO_ACTIVE_VALIDATOR -> R.string.staking_nominator_status_alert_inactive_title to R.string.staking_nominator_status_alert_no_validators
        }
    }

    return resourceManager.getString(titleRes) to resourceManager.getString(messageRes)
}

class WelcomeViewState(
    private val setupStakingSharedState: SetupStakingSharedState,
    private val rewardCalculatorFactory: RewardCalculatorFactory,
    private val resourceManager: ResourceManager,
    private val router: StakingRouter,
    private val scope: CoroutineScope,
    private val errorDisplayer: (String) -> Unit,
    private val validationSystem: WelcomeStakingValidationSystem,
    private val validationExecutor: ValidationExecutor,
    currentAssetFlow: Flow<Asset>,
) : StakingViewState(scope, validationExecutor) {

    private val currentSetupProgress = setupStakingSharedState.get<SetupStakingProcess.Initial>()

    private val rewardCalculator = scope.async { rewardCalculatorFactory.create() }

    private val _showRewardEstimationEvent = MutableLiveData<Event<StakingRewardEstimationBottomSheet.Payload>>()
    val showRewardEstimationEvent: LiveData<Event<StakingRewardEstimationBottomSheet.Payload>> = _showRewardEstimationEvent

    val estimateEarningsTitle = currentAssetFlow.map {
        resourceManager.getString(R.string.staking_estimate_earning_title_v2_2_0, it.token.configuration.symbol)
    }
        .inBackground()
        .share()

    val returns = flowOf {
        val rewardCalculator = rewardCalculator()

        ReturnsModel(
            monthlyPercentage = rewardCalculator.calculateMaxPeriodReturns(PERIOD_MONTH).formatFractionAsPercentage(),
            yearlyPercentage = rewardCalculator.maxCompoundAPY().formatFractionAsPercentage()
        )
    }
        .withLoading()
        .inBackground()
        .share()

    fun infoActionClicked() {
        scope.launch {
            val rewardCalculator = rewardCalculator()

            val payload = StakingRewardEstimationBottomSheet.Payload(
                max = rewardCalculator.maxCompoundAPY().formatFractionAsPercentage(),
                average = rewardCalculator.expectedAPY.formatFractionAsPercentage()
            )

            _showRewardEstimationEvent.value = Event(payload)
        }
    }

    fun nextClicked() {
        scope.launch {
            val payload = WelcomeStakingValidationPayload()

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = payload,
                errorDisplayer = { it.message?.let(errorDisplayer) },
                validationFailureTransformerDefault = { welcomeStakingValidationFailure(it, resourceManager) },
            ) {
                setupStakingSharedState.set(currentSetupProgress.fullFlow())

                router.openSetupStaking()
            }
        }
    }

    private suspend fun rewardCalculator(): RewardCalculator {
        return rewardCalculator.await()
    }
}
