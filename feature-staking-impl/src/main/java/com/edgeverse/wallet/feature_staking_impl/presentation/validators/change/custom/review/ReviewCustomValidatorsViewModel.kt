package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.review

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorDetailsParcelModel
import com.edgeverse.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorModel
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.ValidatorModel
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.review.model.ValidatorsSelectionState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.setCustomValidators
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReviewCustomValidatorsViewModel(
    private val router: StakingRouter,
    private val addressIconGenerator: AddressIconGenerator,
    private val interactor: StakingInteractor,
    private val resourceManager: ResourceManager,
    private val sharedStateSetup: SetupStakingSharedState,
    private val selectedAssetState: SingleAssetSharedState,
    tokenUseCase: TokenUseCase,
) : BaseViewModel() {

    private val confirmSetupState = sharedStateSetup.setupStakingProcess
        .filterIsInstance<SetupStakingProcess.ReadyToSubmit>()
        .share()

    private val selectedValidators = confirmSetupState
        .map { it.payload.validators }
        .share()

    private val currentTokenFlow = tokenUseCase.currentTokenFlow()
        .share()

    private val maxValidatorsPerNominatorFlow = flowOf {
        interactor.maxValidatorsPerNominator()
    }.share()

    val selectionStateFlow = combine(
        selectedValidators,
        maxValidatorsPerNominatorFlow
    ) { validators, maxValidatorsPerNominator ->
        val isOverflow = validators.size > maxValidatorsPerNominator

        ValidatorsSelectionState(
            selectedHeaderText = resourceManager.getString(R.string.staking_custom_header_validators_title, validators.size, maxValidatorsPerNominator),
            isOverflow = isOverflow,
            nextButtonText = if (isOverflow) {
                resourceManager.getString(R.string.staking_custom_proceed_button_disabled_title, maxValidatorsPerNominator)
            } else {
                resourceManager.getString(R.string.common_continue)
            }
        )
    }

    val selectedValidatorModels = combine(
        selectedValidators,
        currentTokenFlow
    ) { validators, token ->
        validators.map { validator ->
            val chain = selectedAssetState.chain()

            mapValidatorToValidatorModel(chain, validator, addressIconGenerator, token)
        }
    }
        .inBackground()
        .share()

    val isInEditMode = MutableStateFlow(false)

    fun deleteClicked(validatorModel: ValidatorModel) {
        launch {
            val validators = selectedValidators.first()

            val withoutRemoved = validators - validatorModel.validator

            sharedStateSetup.setCustomValidators(withoutRemoved)

            if (withoutRemoved.isEmpty()) {
                router.back()
            }
        }
    }

    fun backClicked() {
        router.back()
    }

    fun validatorInfoClicked(validatorModel: ValidatorModel) {
        router.openValidatorDetails(mapValidatorToValidatorDetailsParcelModel(validatorModel.validator))
    }

    fun nextClicked() {
        router.openConfirmStaking()
    }
}
