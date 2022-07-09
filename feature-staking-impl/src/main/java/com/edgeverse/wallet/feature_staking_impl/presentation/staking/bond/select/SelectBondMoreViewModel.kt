package com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.bond.BondMoreInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.bondMoreValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.model.planksFromAmount
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.time.ExperimentalTime

class SelectBondMoreViewModel(
    private val router: StakingRouter,
    private val interactor: StakingInteractor,
    private val bondMoreInteractor: BondMoreInteractor,
    private val resourceManager: ResourceManager,
    private val validationExecutor: ValidationExecutor,
    private val validationSystem: BondMoreValidationSystem,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    private val payload: SelectBondMorePayload,
    amountChooserMixinFactory: AmountChooserMixin.Factory,
    hintsMixinFactory: ResourcesHintsMixinFactory,
) : BaseViewModel(),
    Validatable by validationExecutor,
    FeeLoaderMixin by feeLoaderMixin {

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    private val accountStakingFlow = interactor.selectedAccountStakingStateFlow()
        .filterIsInstance<StakingState.Stash>()
        .inBackground()
        .share()

    private val assetFlow = accountStakingFlow
        .flatMapLatest { interactor.assetFlow(it.stashAddress) }
        .inBackground()
        .share()

    val amountChooserMixin = amountChooserMixinFactory.create(
        scope = this,
        assetFlow = assetFlow,
        balanceField = Asset::transferable,
        balanceLabel = R.string.wallet_balance_transferable
    )

    val hintsMixin = hintsMixinFactory.create(
        coroutineScope = this,
        hintsRes = listOf(R.string.staking_hint_reward_bond_more_v2_2_0)
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
        amountChooserMixin.backPressuredAmount
            .onEach { loadFee(it) }
            .launchIn(viewModelScope)
    }

    private fun loadFee(amount: BigDecimal) {
        feeLoaderMixin.loadFee(
            coroutineScope = viewModelScope,
            feeConstructor = { token ->
                val amountInPlanks = token.planksFromAmount(amount)

                bondMoreInteractor.estimateFee(amountInPlanks)
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
            val payload = BondMoreValidationPayload(
                stashAddress = stashAddress(),
                fee = fee,
                amount = amountChooserMixin.amount.first(),
                chainAsset = assetFlow.first().token.configuration
            )

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = payload,
                validationFailureTransformer = { bondMoreValidationFailure(it, resourceManager) },
                progressConsumer = _showNextProgress.progressConsumer()
            ) {
                _showNextProgress.value = false

                openConfirm(payload)
            }
        }
    }

    private fun openConfirm(validationPayload: BondMoreValidationPayload) {
        val confirmPayload = ConfirmBondMorePayload(
            amount = validationPayload.amount,
            fee = validationPayload.fee,
            stashAddress = validationPayload.stashAddress,
        )

        router.openConfirmBondMore(confirmPayload)
    }

    private suspend fun stashAddress() = accountStakingFlow.first().stashAddress
}
