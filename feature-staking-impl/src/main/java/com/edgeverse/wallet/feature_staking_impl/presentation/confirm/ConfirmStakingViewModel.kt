package com.edgeverse.wallet.feature_staking_impl.presentation.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.address.createAddressModel
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Retriable
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.requireException
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_api.domain.model.RewardDestination
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.setup.BondPayload
import com.edgeverse.wallet.feature_staking_impl.domain.setup.SetupStakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess.ReadyToSubmit.Payload
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationModel
import com.edgeverse.wallet.feature_staking_impl.presentation.common.validation.stakingValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.hints.ConfirmStakeHintsMixinFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.ext.addressOf
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ConfirmStakingViewModel(
    private val router: StakingRouter,
    private val interactor: StakingInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val addressDisplayUseCase: AddressDisplayUseCase,
    private val resourceManager: ResourceManager,
    private val validationSystem: ValidationSystem<SetupStakingPayload, SetupStakingValidationFailure>,
    private val setupStakingSharedState: SetupStakingSharedState,
    private val setupStakingInteractor: SetupStakingInteractor,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    private val externalActions: ExternalActions.Presentation,
    private val selectedAssetState: SingleAssetSharedState,
    private val validationExecutor: ValidationExecutor,
    walletUiUseCase: WalletUiUseCase,
    hintsMixinFactory: ConfirmStakeHintsMixinFactory,
) : BaseViewModel(),
    Retriable,
    Validatable by validationExecutor,
    FeeLoaderMixin by feeLoaderMixin,
    ExternalActions by externalActions {

    private val currentProcessState = setupStakingSharedState.get<SetupStakingProcess.ReadyToSubmit>()
    private val payload = currentProcessState.payload

    val hintsMixin = hintsMixinFactory.create(coroutineScope = this, payload)

    private val bondPayload = when (payload) {
        is Payload.Full -> BondPayload(payload.amount, payload.rewardDestination)
        else -> null
    }

    private val stashFlow = interactor.selectedAccountStakingStateFlow()
        .filterIsInstance<StakingState.Stash>()
        .inBackground()
        .share()

    private val controllerAddressFlow = flowOf {
        when (payload) {
            is Payload.Full -> payload.currentAccountAddress
            else -> stashFlow.first().controllerAddress
        }
    }
        .inBackground()
        .share()

    private val controllerAssetFlow = controllerAddressFlow
        .flatMapLatest { interactor.assetFlow(it) }
        .inBackground()
        .share()

    val title = flowOf {
        when (payload) {
            is Payload.ExistingStash, is Payload.Full -> resourceManager.getString(R.string.staking_start_title)
            is Payload.Validators -> resourceManager.getString(R.string.staking_change_validators)
        }
    }
        .inBackground()
        .share()

    val amountModel = controllerAssetFlow.map { asset ->
        bondPayload?.let {
            mapAmountToAmountModel(it.amount, asset)
        }
    }
        .inBackground()
        .share()

    val walletFlow = walletUiUseCase.selectedWalletUiFlow()
        .inBackground()
        .share()

    val currentAccountModelFlow = controllerAddressFlow.map {
        generateDestinationModel(it, name = null)
    }
        .inBackground()
        .share()

    val nominationsFlow = flowOf {
        val selectedCount = payload.validators.size
        val maxValidatorsPerNominator = interactor.maxValidatorsPerNominator()

        resourceManager.getString(R.string.staking_confirm_nominations, selectedCount, maxValidatorsPerNominator)
    }

    val rewardDestinationFlow = flowOf {
        val rewardDestination = when (payload) {
            is Payload.Full -> payload.rewardDestination
            is Payload.ExistingStash -> interactor.getRewardDestination(stashFlow.first())
            else -> null
        }

        rewardDestination?.let { mapRewardDestinationToRewardDestinationModel(it) }
    }
        .inBackground()
        .share()

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    init {
        loadFee()
    }

    fun confirmClicked() {
        sendTransactionIfValid()
    }

    fun backClicked() {
        router.back()
    }

    fun originAccountClicked() = launch {
        val address = currentAccountModelFlow.first().address

        externalActions.showExternalActions(ExternalActions.Type.Address(address), selectedAssetState.chain())
    }

    fun payoutAccountClicked() = launch {
        val payoutDestination = rewardDestinationFlow.first() as? RewardDestinationModel.Payout ?: return@launch

        val type = ExternalActions.Type.Address(payoutDestination.destination.address)
        externalActions.showExternalActions(type, selectedAssetState.chain())
    }

    fun nominationsClicked() {
        router.openConfirmNominations()
    }

    private fun loadFee() {
        feeLoaderMixin.loadFee(
            viewModelScope,
            feeConstructor = {
                val token = controllerAssetFlow.first().token

                setupStakingInteractor.calculateSetupStakingFee(
                    controllerAddress = controllerAddressFlow.first(),
                    validatorAccountIds = prepareNominations(),
                    bondPayload = bondPayload
                )
            },
            onRetryCancelled = ::backClicked
        )
    }

    private suspend fun mapRewardDestinationToRewardDestinationModel(
        rewardDestination: RewardDestination,
    ): RewardDestinationModel {
        return when (rewardDestination) {
            is RewardDestination.Restake -> RewardDestinationModel.Restake
            is RewardDestination.Payout -> {
                val chain = selectedAssetState.chain()
                val address = chain.addressOf(rewardDestination.targetAccountId)
                val name = addressDisplayUseCase(address)

                val addressModel = generateDestinationModel(address, name)

                RewardDestinationModel.Payout(addressModel)
            }
        }
    }

    private fun prepareNominations() = payload.validators.map(Validator::accountIdHex)

    private fun sendTransactionIfValid() = requireFee { fee ->
        launch {
            val payload = SetupStakingPayload(
                maxFee = fee,
                controllerAddress = controllerAddressFlow.first(),
                bondAmount = bondPayload?.amount,
                asset = controllerAssetFlow.first(),
                isAlreadyNominating = payload !is Payload.Full // not full flow => already nominating
            )

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = payload,
                validationFailureTransformer = { stakingValidationFailure(payload, it, resourceManager) },
                progressConsumer = _showNextProgress.progressConsumer()
            ) {
                sendTransaction(payload)
            }
        }
    }

    private fun sendTransaction(setupStakingPayload: SetupStakingPayload) = launch {
        val setupResult = setupStakingInteractor.setupStaking(
            controllerAddress = setupStakingPayload.controllerAddress,
            validatorAccountIds = prepareNominations(),
            bondPayload = bondPayload
        )

        _showNextProgress.value = false

        if (setupResult.isSuccess) {
            showMessage(resourceManager.getString(R.string.common_transaction_submitted))

            setupStakingSharedState.set(currentProcessState.finish())

            if (currentProcessState.payload is Payload.Validators) {
                router.returnToCurrentValidators()
            } else {
                router.returnToMain()
            }
        } else {
            showError(setupResult.requireException())
        }
    }

    private fun requireFee(block: (BigDecimal) -> Unit) = feeLoaderMixin.requireFee(
        block,
        onError = { title, message -> showError(title, message) }
    )

    private suspend fun generateDestinationModel(address: String, name: String?): AddressModel {
        return addressIconGenerator.createAddressModel(
            accountAddress = address,
            sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
            accountName = name,
            background = AddressIconGenerator.BACKGROUND_TRANSPARENT
        )
    }
}
