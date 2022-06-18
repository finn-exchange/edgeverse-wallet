package com.dfinn.wallet.feature_account_impl.presentation.exporting.json.password

import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.api.Validatable
import com.dfinn.wallet.common.presentation.DescriptiveButtonState
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.validation.progressConsumer
import com.dfinn.wallet.feature_account_impl.R
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.ExportJsonInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations.ExportJsonPasswordValidationPayload
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations.ExportJsonPasswordValidationSystem
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.validations.mapExportJsonPasswordValidationFailureToUi
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.exporting.ExportPayload
import com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ExportJsonPasswordViewModel(
    private val router: AccountRouter,
    private val interactor: ExportJsonInteractor,
    private val resourceManager: ResourceManager,
    private val validationExecutor: ValidationExecutor,
    private val validationSystem: ExportJsonPasswordValidationSystem,
    private val payload: ExportPayload,
) : BaseViewModel(),
    Validatable by validationExecutor {

    val passwordFlow = MutableStateFlow("")
    val passwordConfirmationFlow = MutableStateFlow("")

    private val jsonGenerationInProgressFlow = MutableStateFlow(false)

    val nextButtonState: Flow<DescriptiveButtonState> = combine(
        passwordFlow,
        passwordConfirmationFlow,
        jsonGenerationInProgressFlow
    ) { password, confirmation, jsonGenerationInProgress ->
        when {
            jsonGenerationInProgress -> DescriptiveButtonState.Loading
            password.isBlank() || confirmation.isBlank() -> DescriptiveButtonState.Disabled(
                resourceManager.getString(R.string.common_input_error_set_password)
            )
            else -> DescriptiveButtonState.Enabled(
                resourceManager.getString(R.string.common_continue)
            )
        }
    }

    fun back() {
        router.back()
    }

    fun nextClicked() = viewModelScope.launch {
        val password = passwordFlow.value

        val validationPayload = ExportJsonPasswordValidationPayload(
            password = password,
            passwordConfirmation = passwordConfirmationFlow.value
        )

        validationExecutor.requireValid(
            validationSystem = validationSystem,
            payload = validationPayload,
            progressConsumer = jsonGenerationInProgressFlow.progressConsumer(),
            validationFailureTransformer = { mapExportJsonPasswordValidationFailureToUi(resourceManager, it) }
        ) {
            tryGenerateJson(password)
        }
    }

    private fun tryGenerateJson(password: String) = launch {
        interactor.generateRestoreJson(payload.metaId, payload.chainId, password)
            .onSuccess {
                val confirmPayload = ExportJsonConfirmPayload(payload, it)

                router.openExportJsonConfirm(confirmPayload)
            }
            .onFailure { it.message?.let(::showError) }

        jsonGenerationInProgressFlow.value = false
    }
}
