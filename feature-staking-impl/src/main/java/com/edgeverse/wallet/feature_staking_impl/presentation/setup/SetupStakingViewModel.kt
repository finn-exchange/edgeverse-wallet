package com.edgeverse.wallet.feature_staking_impl.presentation.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Retriable
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.feature_staking_api.domain.model.RewardDestination
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.data.mappers.mapRewardDestinationModelToRewardDestination
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.RewardCalculatorFactory
import com.edgeverse.wallet.feature_staking_impl.domain.setup.SetupStakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationMixin
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.connectWith
import com.edgeverse.wallet.feature_staking_impl.presentation.common.validation.stakingValidationFailure
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SetupStakingViewModel(
    private val router: StakingRouter,
    private val interactor: StakingInteractor,
    private val rewardCalculatorFactory: RewardCalculatorFactory,
    private val resourceManager: ResourceManager,
    private val setupStakingInteractor: SetupStakingInteractor,
    private val validationSystem: ValidationSystem<SetupStakingPayload, SetupStakingValidationFailure>,
    private val setupStakingSharedState: SetupStakingSharedState,
    private val validationExecutor: ValidationExecutor,
    private val feeLoaderMixin: FeeLoaderMixin.Presentation,
    private val rewardDestinationMixin: RewardDestinationMixin.Presentation,
    amountChooserMixinFactory: AmountChooserMixin.Factory,
) : BaseViewModel(),
    Retriable,
    Validatable by validationExecutor,
    FeeLoaderMixin by feeLoaderMixin,
    RewardDestinationMixin by rewardDestinationMixin {

    private val currentProcessState = setupStakingSharedState.get<SetupStakingProcess.Stash>()

    private val _showNextProgress = MutableLiveData(false)
    val showNextProgress: LiveData<Boolean> = _showNextProgress

    private val assetFlow = interactor.currentAssetFlow()
        .share()

    private val rewardCalculator = viewModelScope.async { rewardCalculatorFactory.create() }

    val amountChooserMixin = amountChooserMixinFactory.create(
        scope = this,
        assetFlow = assetFlow,
        balanceField = Asset::transferable,
        balanceLabel = R.string.wallet_balance_transferable
    )

    val title = assetFlow.map { resourceManager.getString(R.string.staking_stake_format, it.token.configuration.symbol) }
        .inBackground()
        .share()

    init {
        loadFee()

        rewardDestinationMixin.connectWith(amountChooserMixin, rewardCalculator = rewardCalculator)
    }

    fun nextClicked() {
        maybeGoToNext()
    }

    fun backClicked() {
        setupStakingSharedState.set(currentProcessState.previous())

        router.back()
    }

    private fun loadFee() {
        feeLoaderMixin.loadFee(
            coroutineScope = viewModelScope,
            feeConstructor = {
                val address = interactor.getSelectedAccountProjection().address

                setupStakingInteractor.estimateMaxSetupStakingFee(address)
            },
            onRetryCancelled = ::backClicked
        )
    }

    private fun maybeGoToNext() = requireFee { fee ->
        launch {
            val rewardDestinationModel = rewardDestinationMixin.rewardDestinationModelFlow.first()
            val rewardDestination = mapRewardDestinationModelToRewardDestination(rewardDestinationModel)
            val amount = amountChooserMixin.amount.first()
            val currentAccountAddress = interactor.getSelectedAccountProjection().address

            val payload = SetupStakingPayload(
                bondAmount = amount,
                controllerAddress = currentAccountAddress,
                maxFee = fee,
                asset = assetFlow.first(),
                isAlreadyNominating = false // on setup staking screen => not nominator
            )

            validationExecutor.requireValid(
                validationSystem = validationSystem,
                payload = payload,
                validationFailureTransformer = { stakingValidationFailure(payload, it, resourceManager) },
                progressConsumer = _showNextProgress.progressConsumer()
            ) {
                _showNextProgress.value = false

                goToNextStep(amount, rewardDestination, currentAccountAddress)
            }
        }
    }

    private fun goToNextStep(
        newAmount: BigDecimal,
        rewardDestination: RewardDestination,
        currentAccountAddress: String
    ) {
        setupStakingSharedState.set(currentProcessState.next(newAmount, rewardDestination, currentAccountAddress))

        router.openStartChangeValidators()
    }

    private fun requireFee(block: (BigDecimal) -> Unit) = feeLoaderMixin.requireFee(
        block,
        onError = { title, message -> showError(title, message) }
    )
}
