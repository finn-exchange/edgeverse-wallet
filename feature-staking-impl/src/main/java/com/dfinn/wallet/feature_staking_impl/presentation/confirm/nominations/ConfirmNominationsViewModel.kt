package com.dfinn.wallet.feature_staking_impl.presentation.confirm.nominations

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_staking_api.domain.model.Validator
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.dfinn.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorDetailsParcelModel
import com.dfinn.wallet.feature_staking_impl.presentation.mappers.mapValidatorToValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.change.ValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.findSelectedValidator
import com.dfinn.wallet.feature_wallet_api.domain.TokenUseCase
import com.dfinn.wallet.feature_wallet_api.domain.model.Token
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.chain
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
