@file:OptIn(ExperimentalTime::class)

package com.dfinn.wallet.feature_staking_impl.presentation.validators.change.custom.search

import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.invoke
import com.dfinn.wallet.common.utils.toggle
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.dfinn.wallet.feature_staking_impl.domain.validators.current.search.SearchCustomValidatorsInteractor
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.dfinn.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorDetailsParcelModel
import com.dfinn.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.ValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.setCustomValidators
import com.dfinn.wallet.feature_wallet_api.domain.TokenUseCase
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.chain
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

sealed class SearchValidatorsState {
    object NoInput : SearchValidatorsState()

    object Loading : SearchValidatorsState()

    object NoResults : SearchValidatorsState()

    class Success(val validators: List<ValidatorModel>, val headerTitle: String) : SearchValidatorsState()
}

class SearchCustomValidatorsViewModel(
    private val router: StakingRouter,
    private val addressIconGenerator: AddressIconGenerator,
    private val interactor: SearchCustomValidatorsInteractor,
    private val resourceManager: ResourceManager,
    private val sharedStateSetup: SetupStakingSharedState,
    private val validatorRecommendatorFactory: ValidatorRecommendatorFactory,
    private val singleAssetSharedState: SingleAssetSharedState,
    tokenUseCase: TokenUseCase,
) : BaseViewModel() {

    private val confirmSetupState = sharedStateSetup.setupStakingProcess
        .filterIsInstance<SetupStakingProcess.ReadyToSubmit>()
        .share()

    private val selectedValidators = confirmSetupState
        .map { it.payload.validators.toSet() }
        .inBackground()
        .share()

    private val currentTokenFlow = tokenUseCase.currentTokenFlow()
        .share()

    val enteredQuery = MutableStateFlow("")

    private val allElectedValidators by lazy {
        async { validatorRecommendatorFactory.create(router.currentStackEntryLifecycle).availableValidators.toSet() }
    }

    private val foundValidatorsState = enteredQuery
        .mapLatest {
            if (it.isNotEmpty()) {
                interactor.searchValidator(it, allElectedValidators() + selectedValidators.first())
            } else {
                null
            }
        }
        .inBackground()
        .share()

    private val selectedValidatorModelsState = combine(
        selectedValidators,
        foundValidatorsState,
        currentTokenFlow
    ) { selectedValidators, foundValidators, token ->
        val chain = singleAssetSharedState.chain()

        foundValidators?.map { validator ->
            mapValidatorToValidatorModel(
                chain = chain,
                validator = validator,
                iconGenerator = addressIconGenerator,
                token = token,
                isChecked = validator in selectedValidators
            )
        }
    }
        .inBackground()
        .share()

    val screenState = selectedValidatorModelsState.map { validators ->
        when {
            validators == null -> SearchValidatorsState.NoInput

            validators.isNullOrEmpty().not() -> {
                SearchValidatorsState.Success(
                    validators = validators,
                    headerTitle = resourceManager.getString(R.string.common_search_results_number, validators.size)
                )
            }

            else -> SearchValidatorsState.NoResults
        }
    }
        .onStart { emit(SearchValidatorsState.Loading) }
        .share()

    fun validatorClicked(validatorModel: ValidatorModel) {
        if (validatorModel.validator.prefs!!.blocked) {
            showError(resourceManager.getString(R.string.staking_custom_blocked_warning))
            return
        }

        launch {
            val newSelected = selectedValidators.first().toggle(validatorModel.validator)

            sharedStateSetup.setCustomValidators(newSelected.toList())
        }
    }

    fun backClicked() {
        router.back()
    }

    fun doneClicked() {
        router.back()
    }

    fun validatorInfoClicked(validatorModel: ValidatorModel) {
        router.openValidatorDetails(mapValidatorToValidatorDetailsParcelModel(validatorModel.validator))
    }
}
