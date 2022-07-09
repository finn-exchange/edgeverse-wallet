package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom

import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CustomContributeViewModel(
    private val customContributeManager: CustomContributeManager,
    val payload: CustomContributePayload,
    private val router: CrowdloanRouter
) : BaseViewModel() {

    val customFlowType = payload.parachainMetadata.customFlow!!

    val viewStateFlow = flow {
        emit(customContributeManager.relevantExtraBonusFlow(customFlowType).createViewState(viewModelScope, payload))
    }.inBackground()
        .share()

    val applyButtonState = viewStateFlow
        .flatMapLatest { it.applyActionState }
        .share()

    private val _applyingInProgress = MutableStateFlow(false)
    val applyingInProgress: Flow<Boolean> = _applyingInProgress

    fun backClicked() {
        router.back()
    }

    fun applyClicked() {
        launch {
            _applyingInProgress.value = true

            viewStateFlow.first().generatePayload()
                .onSuccess {
                    router.setCustomBonus(it)
                    router.back()
                }
                .onFailure(::showError)

            _applyingInProgress.value = false
        }
    }
}
