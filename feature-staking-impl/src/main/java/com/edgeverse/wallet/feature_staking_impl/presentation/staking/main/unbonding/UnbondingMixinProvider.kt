package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding

import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.mixin.actionAwaitable.awaitAction
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.WithCoroutineScopeExtensions
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.domain.model.Unbonding
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnboningsdState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.StakeActionsValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.StakeActionsValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.mainStakingValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding.rebond.RebondKind
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigInteger

class UnbondingMixinFactory(
    private val unbondInteractor: UnbondInteractor,
    private val validationExecutor: ValidationExecutor,
    private val actionAwaitableFactory: ActionAwaitableMixin.Factory,
    private val resourceManager: ResourceManager,
    private val rebondValidationSystem: StakeActionsValidationSystem,
    private val redeemValidationSystem: StakeActionsValidationSystem,
    private val router: StakingRouter,
) {

    fun create(
        errorDisplayer: (Throwable) -> Unit,
        stashState: StakingState.Stash,
        assetFlow: Flow<Asset>,
        coroutineScope: CoroutineScope
    ): UnbondingMixin.Presentation = UnbondingMixinProvider(
        unbondInteractor = unbondInteractor,
        validationExecutor = validationExecutor,
        actionAwaitableFactory = actionAwaitableFactory,
        resourceManager = resourceManager,
        rebondValidationSystem = rebondValidationSystem,
        redeemValidationSystem = redeemValidationSystem,
        router = router,
        errorDisplayer = errorDisplayer,
        stashState = stashState,
        assetFlow = assetFlow,
        coroutineScope = coroutineScope
    )
}

private class UnbondingMixinProvider(
    private val unbondInteractor: UnbondInteractor,
    private val validationExecutor: ValidationExecutor,
    private val actionAwaitableFactory: ActionAwaitableMixin.Factory,
    private val resourceManager: ResourceManager,
    private val rebondValidationSystem: StakeActionsValidationSystem,
    private val redeemValidationSystem: StakeActionsValidationSystem,
    private val router: StakingRouter,
    // From Parent Component
    private val errorDisplayer: (Throwable) -> Unit,
    private val stashState: StakingState.Stash,
    private val assetFlow: Flow<Asset>,
    coroutineScope: CoroutineScope,
) : UnbondingMixin.Presentation,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    override val rebondKindAwaitable = actionAwaitableFactory.create<Unit, RebondKind>()

    private val unbondingsFlow = unbondInteractor.unbondingsFlow(stashState)
        .inBackground()
        .share()

    override val state: Flow<UnbondingMixin.State> = combine(assetFlow, unbondingsFlow) { asset, unbondings ->
        createUiState(unbondings, asset)
    }
        .inBackground()
        .share()

    override fun redeemClicked() = requireValidManageAction(redeemValidationSystem) {
        router.openRedeem()
    }

    override fun cancelClicked() = requireValidManageAction(rebondValidationSystem) {
        launch {
            when (rebondKindAwaitable.awaitAction()) {
                RebondKind.LAST -> openConfirmRebond(unbondInteractor::newestUnbondingAmount)
                RebondKind.ALL -> openConfirmRebond(unbondInteractor::allUnbondingsAmount)
                RebondKind.CUSTOM -> router.openCustomRebond()
            }
        }
    }

    private suspend fun openConfirmRebond(amountBuilder: (List<Unbonding>) -> BigInteger) {
        val unbondingsState = unbondingsFlow.first()
        val asset = assetFlow.first()

        val amountInPlanks = amountBuilder(unbondingsState.unbondings)
        val amount = asset.token.amountFromPlanks(amountInPlanks)

        router.openConfirmRebond(ConfirmRebondPayload(amount))
    }

    private fun createUiState(unbondingsState: UnboningsdState, asset: Asset): UnbondingMixin.State {
        return if (unbondingsState.unbondings.isEmpty()) {
            UnbondingMixin.State.Empty
        } else {
            UnbondingMixin.State.HaveUnbondings(
                redeemEnabled = unbondingsState.anythingToRedeem,
                cancelEnabled = unbondingsState.anythingToUnbond,
                unbondings = unbondingsState.unbondings.mapIndexed { idx, unbonding ->
                    mapUnbondingToUnbondingModel(idx, unbonding, asset)
                }
            )
        }
    }

    private fun mapUnbondingToUnbondingModel(index: Int, unbonding: Unbonding, asset: Asset): UnbondingModel {
        return UnbondingModel(
            index = index,
            status = unbonding.status,
            amountModel = mapAmountToAmountModel(unbonding.amount, asset)
        )
    }

    private fun requireValidManageAction(
        validationSystem: StakeActionsValidationSystem,
        block: (StakeActionsValidationPayload) -> Unit,
    ) {
        launch {
            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = StakeActionsValidationPayload(stashState),
                errorDisplayer = errorDisplayer,
                validationFailureTransformerDefault = { mainStakingValidationFailure(it, resourceManager) },
                block = block
            )
        }
    }
}
