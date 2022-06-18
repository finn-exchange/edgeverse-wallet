package com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.api.Validatable
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.validation.progressConsumer
import com.dfinn.wallet.feature_staking_api.domain.model.StakingState
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationPayload
import com.dfinn.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationSystem
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.unbondPayloadAutoFix
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.unbondValidationFailure
import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import com.dfinn.wallet.feature_wallet_api.domain.model.planksFromAmount
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.dfinn.wallet.feature_wallet_api.presentation.model.transferableAmountModelOf
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.time.ExperimentalTime

class SelectUnbondViewModel(
    private val router: StakingRouter,
    private val interactor: StakingInteractor,
    private val unbondInteractor: UnbondInteractor,
    private val resourceManager: ResourceManager,
    private val validationExecutor: ValidationExecutor,
    private val validationSystem: UnbondValidationSystem,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    unbondHintsMixinFactory: UnbondHintsMixinFactory,
    amountChooserMixinFactory: AmountChooserMixin.Factory
) : BaseViewModel(),
    Validatable by validationExecutor,
    FeeLoaderMixin by feeLoaderMixin {

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    private val accountStakingFlow = interactor.selectedAccountStakingStateFlow()
        .filterIsInstance<StakingState.Stash>()
        .shareInBackground()

    private val assetFlow = accountStakingFlow
        .flatMapLatest { interactor.assetFlow(it.controllerAddress) }
        .shareInBackground()

    val transferableFlow = assetFlow.mapLatest(::transferableAmountModelOf)
        .shareInBackground()

    val hintsMixin = unbondHintsMixinFactory.create(coroutineScope = this)

    val amountMixin = amountChooserMixinFactory.create(
        scope = this,
        assetFlow = assetFlow,
        balanceField = Asset::bonded,
        balanceLabel = R.string.staking_main_stake_balance_staked
    )

    init {
        listenFee()
    }

    fun nextClicked() {
        maybeGoToNext()
    }

    fun backClicked() {
        router.back()
    }

    @OptIn(ExperimentalTime::class)
    private fun listenFee() {
        amountMixin.backPressuredAmount
            .onEach { loadFee(it) }
            .launchIn(viewModelScope)
    }

    private fun loadFee(amount: BigDecimal) {
        feeLoaderMixin.loadFee(
            coroutineScope = viewModelScope,
            feeConstructor = { token ->
                val amountInPlanks = token.planksFromAmount(amount)
                val asset = assetFlow.first()

                unbondInteractor.estimateFee(accountStakingFlow.first(), asset.bondedInPlanks, amountInPlanks)
            },
            onRetryCancelled = ::backClicked
        )
    }

    private fun requireFee(block: (BigDecimal) -> Unit) = feeLoaderMixin.requireFee(
        block,
        onError = { title, message -> showError(title, message) }
    )

    private fun maybeGoToNext() = requireFee { fee ->
        launch {
            val asset = assetFlow.first()

            val payload = UnbondValidationPayload(
                stash = accountStakingFlow.first(),
                asset = asset,
                fee = fee,
                amount = amountMixin.amount.first(),
            )

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = payload,
                validationFailureTransformer = { unbondValidationFailure(it, resourceManager) },
                autoFixPayload = ::unbondPayloadAutoFix,
                progressConsumer = _showNextProgress.progressConsumer()
            ) { correctPayload ->
                _showNextProgress.value = false

                openConfirm(correctPayload)
            }
        }
    }

    private fun openConfirm(validationPayload: UnbondValidationPayload) {
        val confirmUnbondPayload = ConfirmUnbondPayload(
            amount = validationPayload.amount,
            fee = validationPayload.fee
        )

        router.openConfirmUnbond(confirmUnbondPayload)
    }
}
