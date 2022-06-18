package com.dfinn.wallet.feature_staking_impl.presentation.validators.change.recommended

import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.invoke
import com.dfinn.wallet.common.utils.lazyAsync
import com.dfinn.wallet.feature_staking_api.domain.model.Validator
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSettingsProviderFactory
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingProcess.ReadyToSubmit
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingProcess.ReadyToSubmit.SelectionMethod
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.dfinn.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorDetailsParcelModel
import com.dfinn.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.ValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.setRecommendedValidators
import com.dfinn.wallet.feature_wallet_api.domain.TokenUseCase
import com.dfinn.wallet.feature_wallet_api.domain.model.Token
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.chain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecommendedValidatorsViewModel(
    private val router: StakingRouter,
    private val validatorRecommendatorFactory: ValidatorRecommendatorFactory,
    private val recommendationSettingsProviderFactory: RecommendationSettingsProviderFactory,
    private val addressIconGenerator: AddressIconGenerator,
    private val interactor: StakingInteractor,
    private val resourceManager: ResourceManager,
    private val sharedStateSetup: SetupStakingSharedState,
    private val tokenUseCase: TokenUseCase,
    private val selectedAssetState: SingleAssetSharedState
) : BaseViewModel() {

    private val recommendedSettings by lazyAsync {
        recommendationSettingsProviderFactory.create(router.currentStackEntryLifecycle).defaultSettings()
    }

    private val recommendedValidators = flow {
        val validatorRecommendator = validatorRecommendatorFactory.create(router.currentStackEntryLifecycle)
        val validators = validatorRecommendator.recommendations(recommendedSettings())

        emit(validators)
    }.inBackground().share()

    val recommendedValidatorModels = recommendedValidators.map {
        convertToModels(it, tokenUseCase.currentToken())
    }.inBackground().share()

    val selectedTitle = recommendedValidators.map {
        val maxValidators = interactor.maxValidatorsPerNominator()

        resourceManager.getString(R.string.staking_custom_header_validators_title, it.size, maxValidators)
    }.inBackground().share()

    fun backClicked() {
        retractRecommended()

        router.back()
    }

    fun validatorInfoClicked(validatorModel: ValidatorModel) {
        router.openValidatorDetails(mapValidatorToValidatorDetailsParcelModel(validatorModel.validator))
    }

    fun nextClicked() {
        viewModelScope.launch {
            sharedStateSetup.setRecommendedValidators(recommendedValidators.first())

            router.openConfirmStaking()
        }
    }

    private suspend fun convertToModels(
        validators: List<Validator>,
        token: Token
    ): List<ValidatorModel> {
        val chain = selectedAssetState.chain()

        return validators.map {
            mapValidatorToValidatorModel(chain, it, addressIconGenerator, token)
        }
    }

    private fun retractRecommended() = sharedStateSetup.mutate {
        if (it is ReadyToSubmit && it.payload.selectionMethod == SelectionMethod.RECOMMENDED) {
            it.previous()
        } else {
            it
        }
    }
}
