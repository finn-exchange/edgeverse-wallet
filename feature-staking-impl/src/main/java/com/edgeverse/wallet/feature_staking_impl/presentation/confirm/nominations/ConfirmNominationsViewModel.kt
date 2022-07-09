package com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorDetailsParcelModel
import com.edgeverse.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorModel
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.ValidatorModel
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.findSelectedValidator
import com.edgeverse.wallet.feature_wallet_api.domain.TokenUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmNominationsViewModel(
    private val router: StakingRouter,
    private val addressIconGenerator: AddressIconGenerator,
    private val resourceManager: ResourceManager,
    private val sharedStateSetup: SetupStakingSharedState,
    private val selectedAssetState: SingleAssetSharedState,
    private val tokenUseCase: TokenUseCase
) : BaseViewModel() {

    private val currentSetupStakingProcess = sharedStateSetup.get<SetupStakingProcess.ReadyToSubmit>()

    private val validators = currentSetupStakingProcess.payload.validators

    val selectedValidatorsLiveData = liveData(Dispatchers.Default) {
        emit(convertToModels(validators, tokenUseCase.currentToken()))
    }

    val toolbarTitle = selectedValidatorsLiveData.map {
        resourceManager.getString(R.string.staking_selected_validators_mask, it.size)
    }

    fun backClicked() {
        router.back()
    }

    fun validatorInfoClicked(validatorModel: ValidatorModel) {
        viewModelScope.launch {
            validators.findSelectedValidator(validatorModel.accountIdHex)?.let {
                router.openValidatorDetails(mapValidatorToValidatorDetailsParcelModel(it))
            }
        }
    }

    private suspend fun convertToModels(
        validators: List<Validator>,
        token: Token,
    ): List<ValidatorModel> {
        val chain = selectedAssetState.chain()

        return validators.map {
            mapValidatorToValidatorModel(chain, it, addressIconGenerator, token)
        }
    }
}
