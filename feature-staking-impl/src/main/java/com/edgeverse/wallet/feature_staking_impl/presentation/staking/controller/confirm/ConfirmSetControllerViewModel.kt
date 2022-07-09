package com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAccountAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.controller.ControllerInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.set.bondSetControllerValidationFailure
import com.edgeverse.wallet.feature_wallet_api.data.mappers.mapFeeToFeeModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeStatus
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfirmSetControllerViewModel(
    private val router: StakingRouter,
    private val controllerInteractor: ControllerInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val payload: ConfirmSetControllerPayload,
    private val interactor: StakingInteractor,
    private val resourceManager: ResourceManager,
    private val externalActions: ExternalActions.Presentation,
    private val validationExecutor: ValidationExecutor,
    private val validationSystem: SetControllerValidationSystem,
    private val selectedAssetState: SingleAssetSharedState,
    walletUiUseCase: WalletUiUseCase,
) : BaseViewModel(),
    Validatable by validationExecutor,
    ExternalActions by externalActions {

    private val assetFlow = interactor.assetFlow(payload.stashAddress)
        .inBackground()
        .share()

    val feeStatusFlow = assetFlow.map { asset ->
        val feeModel = mapFeeToFeeModel(payload.fee, asset.token)

        FeeStatus.Loaded(feeModel)
    }
        .shareInBackground()

    val stashAddressFlow = flowOf {
        generateIcon(payload.stashAddress)
    }.shareInBackground()

    val controllerAddressLiveData = flowOf {
        generateIcon(payload.controllerAddress)
    }.shareInBackground()

    val walletUiFlow = walletUiUseCase.selectedWalletUiFlow()
        .shareInBackground()

    val submittingInProgress = MutableStateFlow(false)

    fun confirmClicked() {
        maybeConfirm()
    }

    fun stashClicked() {
        showExternalActions(payload.stashAddress)
    }

    fun controllerClicked() {
        showExternalActions(payload.controllerAddress)
    }

    fun back() {
        router.back()
    }

    private fun showExternalActions(address: String) = launch {
        externalActions.showExternalActions(ExternalActions.Type.Address(address), selectedAssetState.chain())
    }

    private fun maybeConfirm() = launch {

        val payload = SetControllerValidationPayload(
            stashAddress = payload.stashAddress,
            controllerAddress = payload.controllerAddress,
            fee = payload.fee,
            transferable = payload.transferable
        )

        validationExecutor.requireValid(
            validationSystem = validationSystem,
            payload = payload,
            progressConsumer = submittingInProgress.progressConsumer(),
            validationFailureTransformer = { bondSetControllerValidationFailure(it, resourceManager) }
        ) {
            sendTransaction()
        }
    }

    private fun sendTransaction() = launch {
        val result = controllerInteractor.setController(
            stashAccountAddress = payload.stashAddress,
            controllerAccountAddress = payload.controllerAddress
        )

        submittingInProgress.value = false

        if (result.isSuccess) {
            showMessage(resourceManager.getString(R.string.staking_controller_change_success))

            router.returnToMain()
        }
    }

    private suspend fun generateIcon(address: String) = addressIconGenerator.createAccountAddressModel(
        chain = selectedAssetState.chain(),
        address = address,
    )
}
