package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAccountAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rebond.RebondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.rebondValidationFailure
import com.edgeverse.wallet.feature_wallet_api.domain.model.planksFromAmount
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.requireFee
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfirmRebondViewModel(
    private val router: StakingRouter,
    interactor: StakingInteractor,
    private val rebondInteractor: RebondInteractor,
    private val resourceManager: ResourceManager,
    private val validationExecutor: ValidationExecutor,
    private val validationSystem: RebondValidationSystem,
    private val iconGenerator: AddressIconGenerator,
    private val externalActions: ExternalActions.Presentation,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    private val payload: ConfirmRebondPayload,
    private val selectedAssetState: SingleAssetSharedState,
    hintsMixinFactory: ResourcesHintsMixinFactory,
    walletUiUseCase: WalletUiUseCase,
) : BaseViewModel(),
    ExternalActions by externalActions,
    FeeLoaderMixin by feeLoaderMixin,
    Validatable by validationExecutor {

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    private val accountStakingFlow = interactor.selectedAccountStakingStateFlow()
        .filterIsInstance<StakingState.Stash>()
        .inBackground()
        .share()

    private val assetFlow = accountStakingFlow.flatMapLatest {
        interactor.assetFlow(it.controllerAddress)
    }
        .inBackground()
        .share()

    val hintsMixin = hintsMixinFactory.create(
        coroutineScope = this,
        hintsRes = listOf(R.string.staking_rebond_counted_next_era_hint)
    )

    val walletUiFlow = walletUiUseCase.selectedWalletUiFlow()
        .shareInBackground()

    val amountModelFlow = assetFlow.map { asset ->
        mapAmountToAmountModel(payload.amount, asset)
    }
        .shareInBackground()

    val originAddressModelFlow = accountStakingFlow.map {
        iconGenerator.createAccountAddressModel(it.chain, it.controllerAddress)
    }.shareInBackground()

    init {
        loadFee()
    }

    fun confirmClicked() {
        maybeGoToNext()
    }

    fun backClicked() {
        router.back()
    }

    fun originAccountClicked() {
        launch {
            val payload = ExternalActions.Type.Address(originAddressModelFlow.first().address)

            externalActions.showExternalActions(payload, selectedAssetState.chain())
        }
    }

    private fun loadFee() {
        feeLoaderMixin.loadFee(
            coroutineScope = viewModelScope,
            feeConstructor = { token ->
                val amountInPlanks = token.planksFromAmount(payload.amount)

                rebondInteractor.estimateFee(amountInPlanks)
            },
            onRetryCancelled = ::backClicked
        )
    }

    private fun maybeGoToNext() = feeLoaderMixin.requireFee(this) { fee ->
        launch {
            val payload = RebondValidationPayload(
                fee = fee,
                rebondAmount = payload.amount,
                controllerAsset = assetFlow.first()
            )

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = payload,
                validationFailureTransformer = { rebondValidationFailure(it, resourceManager) },
                progressConsumer = _showNextProgress.progressConsumer(),
                block = ::sendTransaction
            )
        }
    }

    private fun sendTransaction(validPayload: RebondValidationPayload) = launch {
        val amountInPlanks = validPayload.controllerAsset.token.planksFromAmount(payload.amount)
        val stashState = accountStakingFlow.first()

        rebondInteractor.rebond(stashState, amountInPlanks)
            .onSuccess {
                showMessage(resourceManager.getString(R.string.common_transaction_submitted))

                router.returnToMain()
            }
            .onFailure(::showError)

        _showNextProgress.value = false
    }
}
