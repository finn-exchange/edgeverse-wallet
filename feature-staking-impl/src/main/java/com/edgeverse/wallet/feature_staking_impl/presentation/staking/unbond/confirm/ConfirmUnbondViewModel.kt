package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.requireException
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAccountAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.unbondPayloadAutoFix
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.unbondValidationFailure
import com.edgeverse.wallet.feature_wallet_api.data.mappers.mapFeeToFeeModel
import com.edgeverse.wallet.feature_wallet_api.domain.model.planksFromAmount
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeStatus
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfirmUnbondViewModel(
    private val router: StakingRouter,
    interactor: StakingInteractor,
    private val unbondInteractor: UnbondInteractor,
    private val resourceManager: ResourceManager,
    private val validationExecutor: ValidationExecutor,
    private val iconGenerator: AddressIconGenerator,
    private val validationSystem: UnbondValidationSystem,
    private val externalActions: ExternalActions.Presentation,
    private val payload: ConfirmUnbondPayload,
    private val selectedAssetState: SingleAssetSharedState,
    unbondHintsMixinFactory: UnbondHintsMixinFactory,
    walletUiUseCase: WalletUiUseCase,
) : BaseViewModel(),
    ExternalActions by externalActions,
    Validatable by validationExecutor {

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    val hintsMixin = unbondHintsMixinFactory.create(coroutineScope = this)

    private val accountStakingFlow = interactor.selectedAccountStakingStateFlow()
        .filterIsInstance<StakingState.Stash>()
        .inBackground()
        .share()

    private val assetFlow = accountStakingFlow.flatMapLatest {
        interactor.assetFlow(it.controllerAddress)
    }
        .inBackground()
        .share()

    val walletUiFlow = walletUiUseCase.selectedWalletUiFlow()
        .shareInBackground()

    val amountModelFlow = assetFlow.map { asset ->
        mapAmountToAmountModel(payload.amount, asset)
    }
        .shareInBackground()

    val feeStatusLiveData = assetFlow.map { asset ->
        val feeModel = mapFeeToFeeModel(payload.fee, asset.token)

        FeeStatus.Loaded(feeModel)
    }
        .inBackground()
        .asLiveData()

    val originAddressModelFlow = accountStakingFlow.map {
        iconGenerator.createAccountAddressModel(it.chain, it.controllerAddress)
    }
        .shareInBackground()

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

    private fun maybeGoToNext() = launch {
        val asset = assetFlow.first()

        val payload = UnbondValidationPayload(
            asset = asset,
            stash = accountStakingFlow.first(),
            fee = payload.fee,
            amount = payload.amount,
        )

        validationExecutor.requireValid(
            validationSystem = validationSystem,
            payload = payload,
            validationFailureTransformer = { unbondValidationFailure(it, resourceManager) },
            autoFixPayload = ::unbondPayloadAutoFix,
            progressConsumer = _showNextProgress.progressConsumer()
        ) { validPayload ->
            sendTransaction(validPayload)
        }
    }

    private fun sendTransaction(validPayload: UnbondValidationPayload) = launch {
        val amountInPlanks = validPayload.asset.token.configuration.planksFromAmount(payload.amount)

        val result = unbondInteractor.unbond(validPayload.stash, validPayload.asset.bondedInPlanks, amountInPlanks)

        _showNextProgress.value = false

        if (result.isSuccess) {
            showMessage(resourceManager.getString(R.string.common_transaction_submitted))

            router.returnToMain()
        } else {
            showError(result.requireException())
        }
    }
}
