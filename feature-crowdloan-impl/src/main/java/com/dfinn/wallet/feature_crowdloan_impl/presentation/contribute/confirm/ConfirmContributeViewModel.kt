package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.address.createAddressModel
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.api.Validatable
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.flowOf
import com.dfinn.wallet.common.utils.formatAsCurrency
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.validation.CompositeValidation
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.validation.ValidationSystem
import com.dfinn.wallet.common.validation.progressConsumer
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.domain.model.defaultSubstrateAddress
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_crowdloan_impl.R
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.CrowdloanContributeInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidation
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidationPayload
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm.model.LeasePeriodModel
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm.parcel.ConfirmContributePayload
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.contributeValidationFailure
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.ConfirmContributeCustomization
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.mapParachainMetadataFromParcel
import com.dfinn.wallet.feature_wallet_api.data.mappers.mapAssetToAssetModel
import com.dfinn.wallet.feature_wallet_api.data.mappers.mapFeeToFeeModel
import com.dfinn.wallet.feature_wallet_api.domain.AssetUseCase
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeStatus
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.chain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ConfirmContributeViewModel(
    private val router: CrowdloanRouter,
    private val contributionInteractor: CrowdloanContributeInteractor,
    private val resourceManager: ResourceManager,
    assetUseCase: AssetUseCase,
    accountUseCase: SelectedAccountUseCase,
    addressModelGenerator: AddressIconGenerator,
    private val validationExecutor: ValidationExecutor,
    private val payload: ConfirmContributePayload,
    private val validations: Collection<ContributeValidation>,
    private val customContributeManager: CustomContributeManager,
    private val externalActions: ExternalActions.Presentation,
    private val assetSharedState: SingleAssetSharedState,
) : BaseViewModel(),
    Validatable by validationExecutor,
    ExternalActions by externalActions {

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    private val assetFlow = assetUseCase.currentAssetFlow()
        .share()

    val assetModelFlow = assetFlow
        .map { mapAssetToAssetModel(it, resourceManager) }
        .inBackground()
        .share()

    val selectedAddressModelFlow = accountUseCase.selectedMetaAccountFlow()
        .map {
            addressModelGenerator.createAddressModel(it.defaultSubstrateAddress, AddressIconGenerator.SIZE_SMALL, it.name)
        }

    val selectedAmount = payload.amount.toString()

    val feeFlow = assetFlow.map { asset ->
        val feeModel = mapFeeToFeeModel(payload.fee, asset.token)

        FeeStatus.Loaded(feeModel)
    }
        .inBackground()
        .share()

    val enteredFiatAmountFlow = assetFlow.map { asset ->
        asset.token.fiatAmount(payload.amount).formatAsCurrency()
    }
        .inBackground()
        .share()

    private val parachainMetadata = payload.metadata?.let(::mapParachainMetadataFromParcel)

    private val relevantCustomFlowFactory = parachainMetadata?.customFlow?.let {
        customContributeManager.getFactoryOrNull(it)
    }

    val customizationConfiguration: Flow<Pair<ConfirmContributeCustomization, ConfirmContributeCustomization.ViewState>?> = flowOf {
        relevantCustomFlowFactory?.confirmContributeCustomization?.let {
            it to it.createViewState(coroutineScope = this, parachainMetadata!!, payload.customizationPayload)
        }
    }
        .inBackground()
        .share()

    val estimatedReward = payload.estimatedRewardDisplay

    private val crowdloanFlow = contributionInteractor.crowdloanStateFlow(payload.paraId, parachainMetadata)
        .inBackground()
        .share()

    val crowdloanInfoFlow = crowdloanFlow.map { crowdloan ->
        LeasePeriodModel(
            leasePeriod = resourceManager.formatDuration(crowdloan.leasePeriodInMillis),
            leasedUntil = resourceManager.formatDate(crowdloan.leasedUntilInMillis)
        )
    }
        .inBackground()
        .share()

    val bonusFlow = flow {
        val bonusDisplay = payload.bonusPayload?.bonusText(payload.amount)

        emit(bonusDisplay)
    }
        .inBackground()
        .share()

    private val customizedValidationSystem = flowOf {
        val validations = relevantCustomFlowFactory?.confirmContributeCustomization?.modifyValidations(validations)
            ?: validations

        ValidationSystem(CompositeValidation(validations))
    }
        .inBackground()
        .share()

    fun nextClicked() {
        maybeGoToNext()
    }

    fun backClicked() {
        router.back()
    }

    fun originAccountClicked() {
        launch {
            val accountAddress = selectedAddressModelFlow.first().address
            val chain = assetSharedState.chain()

            externalActions.showExternalActions(ExternalActions.Type.Address(accountAddress), chain)
        }
    }

    private fun maybeGoToNext() = launch {
        val validationPayload = ContributeValidationPayload(
            crowdloan = crowdloanFlow.first(),
            fee = payload.fee,
            asset = assetFlow.first(),
            customizationPayload = payload.customizationPayload,
            bonusPayload = payload.bonusPayload,
            contributionAmount = payload.amount
        )

        validationExecutor.requireValid(
            validationSystem = customizedValidationSystem.first(),
            payload = validationPayload,
            progressConsumer = _showNextProgress.progressConsumer(),
            validationFailureTransformerCustom = { status, actions ->
                contributeValidationFailure(
                    reason = status.reason,
                    validationFlowActions = actions,
                    resourceManager = resourceManager,
                    onOpenCustomContribute = null
                )
            }
        ) {
            sendTransaction()
        }
    }

    private fun sendTransaction() {
        launch {
            val crowdloan = crowdloanFlow.first()

            contributionInteractor.contribute(
                crowdloan = crowdloan,
                contribution = payload.amount,
                bonusPayload = payload.bonusPayload,
                customizationPayload = payload.customizationPayload
            )
                .onFailure(::showError)
                .onSuccess {
                    showMessage(resourceManager.getString(R.string.common_transaction_submitted))

                    router.returnToMain()
                }

            _showNextProgress.value = false
        }
    }
}
