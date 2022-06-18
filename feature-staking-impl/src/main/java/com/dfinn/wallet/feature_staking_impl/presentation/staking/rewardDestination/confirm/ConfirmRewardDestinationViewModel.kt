package com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.api.Validatable
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.flowOf
import com.dfinn.wallet.common.utils.requireException
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.validation.progressConsumer
import com.dfinn.wallet.feature_account_api.presenatation.account.icon.createAccountAddressModel
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_staking_api.domain.model.RewardDestination
import com.dfinn.wallet.feature_staking_api.domain.model.StakingState
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.data.mappers.mapRewardDestinationModelToRewardDestination
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.staking.rewardDestination.ChangeRewardDestinationInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationPayload
import com.dfinn.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationSystem
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationModel
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.ConfirmRewardDestinationPayload
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.RewardDestinationParcelModel
import com.dfinn.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.rewardDestinationValidationFailure
import com.dfinn.wallet.feature_wallet_api.data.mappers.mapFeeToFeeModel
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeStatus
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.chain
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfirmRewardDestinationViewModel(
    private val router: StakingRouter,
    private val interactor: StakingInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val resourceManager: ResourceManager,
    private val validationSystem: RewardDestinationValidationSystem,
    private val rewardDestinationInteractor: ChangeRewardDestinationInteractor,
    private val externalActions: ExternalActions.Presentation,
    private val validationExecutor: ValidationExecutor,
    private val payload: ConfirmRewardDestinationPayload,
    private val selectedAssetState: SingleAssetSharedState,
    walletUiUseCase: WalletUiUseCase,
) : BaseViewModel(),
    Validatable by validationExecutor,
    ExternalActions by externalActions {

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    private val stashFlow = interactor.selectedAccountStakingStateFlow()
        .filterIsInstance<StakingState.Stash>()
        .shareInBackground()

    private val controllerAssetFlow = stashFlow
        .flatMapLatest { interactor.assetFlow(it.controllerAddress) }
        .shareInBackground()

    val walletUiFlow = walletUiUseCase.selectedWalletUiFlow()
        .shareInBackground()

    val originAccountModelFlow = stashFlow.map {
        addressIconGenerator.createAccountAddressModel(it.chain, it.controllerAddress)
    }
        .shareInBackground()

    val rewardDestinationFlow = flowOf {
        mapRewardDestinationParcelModelToRewardDestinationModel(payload.rewardDestination)
    }
        .shareInBackground()

    val feeStatusFlow = controllerAssetFlow.map {
        FeeStatus.Loaded(mapFeeToFeeModel(payload.fee, it.token))
    }
        .shareInBackground()

    fun confirmClicked() {
        sendTransactionIfValid()
    }

    fun backClicked() {
        router.back()
    }

    fun originAccountClicked() = launch {
        val originAddress = originAccountModelFlow.first().address

        showAddressExternalActions(originAddress)
    }

    fun payoutAccountClicked() = launch {
        val payoutDestination = rewardDestinationFlow.first() as? RewardDestinationModel.Payout ?: return@launch

        showAddressExternalActions(payoutDestination.destination.address)
    }

    private suspend fun showAddressExternalActions(address: String) {
        externalActions.showExternalActions(ExternalActions.Type.Address(address), selectedAssetState.chain())
    }

    private suspend fun mapRewardDestinationParcelModelToRewardDestinationModel(
        rewardDestinationParcelModel: RewardDestinationParcelModel,
    ): RewardDestinationModel {
        return when (rewardDestinationParcelModel) {
            is RewardDestinationParcelModel.Restake -> RewardDestinationModel.Restake
            is RewardDestinationParcelModel.Payout -> {
                val address = rewardDestinationParcelModel.targetAccountAddress
                val addressModel = addressIconGenerator.createAccountAddressModel(selectedAssetState.chain(), address)

                RewardDestinationModel.Payout(addressModel)
            }
        }
    }

    private fun sendTransactionIfValid() = launch {
        val rewardDestinationModel = rewardDestinationFlow.first()

        val controllerAsset = controllerAssetFlow.first()
        val stashState = stashFlow.first()

        val payload = RewardDestinationValidationPayload(
            availableControllerBalance = controllerAsset.transferable,
            fee = payload.fee,
            stashState = stashState
        )

        validationExecutor.requireValid(
            validationSystem = validationSystem,
            payload = payload,
            validationFailureTransformer = { rewardDestinationValidationFailure(resourceManager, it) },
            progressConsumer = _showNextProgress.progressConsumer()
        ) {
            sendTransaction(stashState, mapRewardDestinationModelToRewardDestination(rewardDestinationModel))
        }
    }

    private fun sendTransaction(
        stashState: StakingState.Stash,
        rewardDestination: RewardDestination,
    ) = launch {
        val setupResult = rewardDestinationInteractor.changeRewardDestination(stashState, rewardDestination)

        _showNextProgress.value = false

        if (setupResult.isSuccess) {
            showMessage(resourceManager.getString(R.string.common_transaction_submitted))

            router.returnToMain()
        } else {
            showError(setupResult.requireException())
        }
    }
}
