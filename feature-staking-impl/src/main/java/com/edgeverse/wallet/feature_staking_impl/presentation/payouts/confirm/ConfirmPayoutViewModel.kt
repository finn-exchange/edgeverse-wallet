package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.base.TitleAndMessage
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.requireException
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAccountAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.data.model.Payout
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.payout.PayoutInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.MakePayoutPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.PayoutValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.requireFee
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfirmPayoutViewModel(
    private val interactor: StakingInteractor,
    private val payoutInteractor: PayoutInteractor,
    private val router: StakingRouter,
    private val payload: ConfirmPayoutPayload,
    private val addressModelGenerator: AddressIconGenerator,
    private val externalActions: ExternalActions.Presentation,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    private val validationSystem: ValidationSystem<MakePayoutPayload, PayoutValidationFailure>,
    private val validationExecutor: ValidationExecutor,
    private val resourceManager: ResourceManager,
    private val selectedAssetState: SingleAssetSharedState,
    walletUiUseCase: WalletUiUseCase,
) : BaseViewModel(),
    ExternalActions.Presentation by externalActions,
    FeeLoaderMixin by feeLoaderMixin,
    Validatable by validationExecutor {

    private val assetFlow = interactor.currentAssetFlow()
        .share()

    private val stakingStateFlow = interactor.selectedAccountStakingStateFlow()
        .share()

    private val payouts = payload.payouts.map { Payout(it.validatorInfo.address, it.era, it.amountInPlanks) }

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    val totalRewardFlow = assetFlow.map {
        mapAmountToAmountModel(payload.totalRewardInPlanks, it)
    }
        .shareInBackground()

    val walletUiFlow = walletUiUseCase.selectedWalletUiFlow()
        .shareInBackground()

    val initiatorAddressModel = stakingStateFlow.map { stakingState ->
        addressModelGenerator.createAccountAddressModel(selectedAssetState.chain(), stakingState.accountAddress)
    }
        .shareInBackground()

    init {
        loadFee()
    }

    fun accountClicked() {
        launch {
            val address = initiatorAddressModel.first().address

            externalActions.showExternalActions(ExternalActions.Type.Address(address), selectedAssetState.chain())
        }
    }

    fun submitClicked() {
        sendTransactionIfValid()
    }

    fun backClicked() {
        router.back()
    }

    private fun sendTransactionIfValid() = feeLoaderMixin.requireFee(this) { fee ->
        launch {
            val tokenType = assetFlow.first().token.configuration
            val accountAddress = stakingStateFlow.first().accountAddress
            val amount = tokenType.amountFromPlanks(payload.totalRewardInPlanks)

            val payoutStakersPayloads = payouts.map { MakePayoutPayload.PayoutStakersPayload(it.era, it.validatorAddress) }

            val makePayoutPayload = MakePayoutPayload(accountAddress, fee, amount, tokenType, payoutStakersPayloads)

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = makePayoutPayload,
                validationFailureTransformer = ::payloadValidationFailure,
                progressConsumer = _showNextProgress.progressConsumer()
            ) {
                sendTransaction(makePayoutPayload)
            }
        }
    }

    private fun sendTransaction(payload: MakePayoutPayload) = launch {
        val result = payoutInteractor.makePayouts(payload)

        _showNextProgress.value = false

        if (result.isSuccess) {
            showMessage(resourceManager.getString(R.string.make_payout_transaction_sent))

            router.returnToMain()
        } else {
            showError(result.requireException())
        }
    }

    private fun loadFee() {
        feeLoaderMixin.loadFee(
            viewModelScope,
            feeConstructor = {
                val address = stakingStateFlow.first().accountAddress

                payoutInteractor.estimatePayoutFee(address, payouts)
            },
            onRetryCancelled = ::backClicked
        )
    }

    private fun payloadValidationFailure(reason: PayoutValidationFailure): TitleAndMessage {
        val (titleRes, messageRes) = when (reason) {
            PayoutValidationFailure.CannotPayFee -> R.string.common_not_enough_funds_title to R.string.common_not_enough_funds_message
            PayoutValidationFailure.UnprofitablePayout -> R.string.common_confirmation_title to R.string.staking_warning_tiny_payout
        }

        return resourceManager.getString(titleRes) to resourceManager.getString(messageRes)
    }
}
