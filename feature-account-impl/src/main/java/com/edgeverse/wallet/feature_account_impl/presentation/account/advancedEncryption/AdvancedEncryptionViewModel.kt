package com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.input.*
import com.edgeverse.wallet.common.utils.singleReplaySharedFlow
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.core.model.CryptoType
import com.edgeverse.wallet.feature_account_impl.data.mappers.mapCryptoTypeToCryptoTypeModel
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationPayload
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationSystem
import com.edgeverse.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.mapAdvancedEncryptionValidationFailureToUi
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionResponder
import com.edgeverse.wallet.feature_account_impl.presentation.view.advanced.encryption.model.CryptoTypeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AdvancedEncryptionViewModel(
    private val router: AccountRouter,
    private val payload: AdvancedEncryptionPayload,
    private val interactor: AdvancedEncryptionInteractor,
    private val resourceManager: ResourceManager,
    private val validationSystem: AdvancedEncryptionValidationSystem,
    private val validationExecutor: ValidationExecutor,
    private val advancedEncryptionResponder: AdvancedEncryptionResponder,
) : BaseViewModel(),
    Validatable by validationExecutor {

    private val encryptionTypes = getCryptoTypeModels()

    private val _substrateCryptoTypeInput = singleReplaySharedFlow<Input<CryptoTypeModel>>()
    val substrateCryptoTypeInput: Flow<Input<CryptoTypeModel>> = _substrateCryptoTypeInput

    private val _substrateDerivationPathInput = singleReplaySharedFlow<Input<String>>()
    val substrateDerivationPathInput: Flow<Input<String>> = _substrateDerivationPathInput

    private val _ethereumCryptoTypeInput = singleReplaySharedFlow<Input<CryptoTypeModel>>()
    val ethereumCryptoTypeInput: Flow<Input<CryptoTypeModel>> = _ethereumCryptoTypeInput

    private val _ethereumDerivationPathInput = singleReplaySharedFlow<Input<String>>()
    val ethereumDerivationPathInput: Flow<Input<String>> = _ethereumDerivationPathInput

    val showSubstrateEncryptionTypeChooserEvent = MutableLiveData<Event<DynamicListBottomSheet.Payload<CryptoTypeModel>>>()

    val applyVisible = payload is AdvancedEncryptionPayload.Change

    init {
        loadInitialState()
    }

    private fun loadInitialState() = launch {
        val initialState = interactor.getInitialInputState(payload)

        val latestState = advancedEncryptionResponder.lastState

        val initialSubstrateType = initialState.substrateCryptoType.modifyIfNotNull(latestState?.substrateCryptoType)
        val initialSubstrateDerivationPath = initialState.substrateDerivationPath.modifyIfNotNull(latestState?.substrateDerivationPath)
        val initialEthereumType = initialState.ethereumCryptoType.modifyIfNotNull(latestState?.ethereumCryptoType)
        val initialEthereumDerivationPath = initialState.ethereumDerivationPath.modifyIfNotNull(latestState?.ethereumDerivationPath)

        _substrateCryptoTypeInput.emit(initialSubstrateType.map(::encryptionTypeToUi))
        _substrateDerivationPathInput.emit(initialSubstrateDerivationPath)
        _ethereumCryptoTypeInput.emit(initialEthereumType.map(::encryptionTypeToUi))
        _ethereumDerivationPathInput.emit(initialEthereumDerivationPath)
    }

    fun substrateDerivationPathChanged(new: String) = _substrateDerivationPathInput.modifyInputAsync(new)

    fun ethereumDerivationPathChanged(new: String) = _ethereumDerivationPathInput.modifyInputAsync(new)

    fun substrateEncryptionClicked() = substrateCryptoTypeInput.ifModifiable { current ->
        showSubstrateEncryptionTypeChooserEvent.value = Event(DynamicListBottomSheet.Payload(encryptionTypes, current))
    }

    fun substrateEncryptionChanged(newCryptoType: CryptoTypeModel) {
        launch {
            _substrateCryptoTypeInput.modifyInput(newCryptoType)
        }
    }

    fun homeButtonClicked() {
        router.back()
    }

    fun applyClicked() = launch {
        val payload = AdvancedEncryptionValidationPayload(
            substrateDerivationPathInput = substrateDerivationPathInput.first(),
            ethereumDerivationPathInput = ethereumDerivationPathInput.first()
        )

        validationExecutor.requireValid(
            validationSystem = validationSystem,
            payload = payload,
            validationFailureTransformer = { mapAdvancedEncryptionValidationFailureToUi(resourceManager, it) }
        ) {
            respondWithCurrentState()
        }
    }

    private fun respondWithCurrentState() = launch {
        val response = AdvancedEncryptionCommunicator.Response(
            substrateCryptoType = substrateCryptoTypeInput.first().valueOrNull?.cryptoType,
            substrateDerivationPath = substrateDerivationPathInput.first().valueOrNull,
            ethereumCryptoType = ethereumCryptoTypeInput.first().valueOrNull?.cryptoType,
            ethereumDerivationPath = ethereumDerivationPathInput.first().valueOrNull,
        )

        advancedEncryptionResponder.respond(response)

        router.back()
    }

    private fun getCryptoTypeModels(): List<CryptoTypeModel> {
        val types = interactor.getCryptoTypes()

        return types.map { mapCryptoTypeToCryptoTypeModel(resourceManager, it) }
    }

    private fun <I> Flow<Input<I>>.ifModifiable(action: suspend (I) -> Unit) {
        launch {
            first().ifModifiable { action(it) }
        }
    }

    private fun <I> MutableSharedFlow<Input<I>>.modifyInputAsync(newValue: I) {
        launch {
            modifyInput(newValue)
        }
    }

    private fun encryptionTypeToUi(encryptionType: CryptoType): CryptoTypeModel = mapCryptoTypeToCryptoTypeModel(resourceManager, encryptionType)
}
