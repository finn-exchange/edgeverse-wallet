package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Browserable
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsPayload
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsValidationSystem
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.mapParachainMetadataFromParcel
import com.edgeverse.wallet.feature_wallet_api.domain.AssetUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.getCurrentAsset
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

sealed class SubmitActionState {
    object Loading : SubmitActionState()

    object Available : SubmitActionState()

    class Unavailable(val reason: String) : SubmitActionState()
}

class TermsLinkContent(
    val title: String,
    val iconUrl: String,
)

class MoonbeamCrowdloanTermsViewModel(
    private val interactor: MoonbeamCrowdloanInteractor,
    val payload: ContributePayload,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    private val resourceManager: ResourceManager,
    private val router: CrowdloanRouter,
    private val assetUseCase: AssetUseCase,
    private val validationExecutor: ValidationExecutor,
    private val validationSystem: MoonbeamTermsValidationSystem,
) : BaseViewModel(),
    FeeLoaderMixin by feeLoaderMixin,
    Validatable by validationExecutor,
    Browserable {

    init {
        loadFee()
    }

    val termsLinkContent = TermsLinkContent(
        title = resourceManager.getString(R.string.crowdloan_terms_conditions_named, payload.parachainMetadata!!.name),
        iconUrl = payload.parachainMetadata.iconLink
    )

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    val termsCheckedFlow = MutableStateFlow(false)

    private val submittingInProgressFlow = MutableStateFlow(false)

    private val parachainMetadata = flowOf { mapParachainMetadataFromParcel(payload.parachainMetadata!!) }
        .inBackground()
        .share()

    val submitButtonState = combine(
        termsCheckedFlow,
        submittingInProgressFlow
    ) { termsChecked, submitInProgress ->
        when {
            submitInProgress -> SubmitActionState.Loading
            termsChecked -> SubmitActionState.Available
            else -> SubmitActionState.Unavailable(
                reason = resourceManager.getString(R.string.crowdloan_agree_with_policy)
            )
        }
    }

    fun backClicked() {
        router.back()
    }

    fun termsLinkClicked() {
        openBrowserEvent.value = Event(interactor.getTermsLink())
    }

    fun submitClicked() = requireFee { fee ->
        submittingInProgressFlow.value = true

        submitAfterValidation(fee)
    }

    private fun submitAfterValidation(fee: BigDecimal) = launch {
        val validationPayload = MoonbeamTermsPayload(
            fee = fee,
            asset = assetUseCase.getCurrentAsset()
        )

        validationExecutor.requireValid(
            validationSystem = validationSystem,
            payload = validationPayload,
            validationFailureTransformer = { moonbeamTermsValidationFailure(it, resourceManager) },
            progressConsumer = submittingInProgressFlow.progressConsumer()
        ) {
            submit()
        }
    }

    private fun submit() = launch {
        interactor.submitAgreement(parachainMetadata.first())
            .onFailure(::showError)
            .onSuccess {
                router.openContribute(payload)
            }

        submittingInProgressFlow.value = false
    }

    private fun loadFee() = launch {
        feeLoaderMixin.loadFee(
            coroutineScope = this,
            feeConstructor = {
                interactor.calculateTermsFee()
            },
            onRetryCancelled = ::backClicked
        )
    }

    private fun requireFee(block: (BigDecimal) -> Unit) = feeLoaderMixin.requireFee(
        block,
        onError = { title, message -> showError(title, message) }
    )
}
