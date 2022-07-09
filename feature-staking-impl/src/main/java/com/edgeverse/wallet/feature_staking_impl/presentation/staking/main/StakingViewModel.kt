package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main

import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.address.createAddressModel
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.common.presentation.flatMapLoading
import com.edgeverse.wallet.common.presentation.mapLoading
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.*
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.alerts.Alert
import com.edgeverse.wallet.feature_staking_impl.domain.alerts.AlertsInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.model.NetworkInfo
import com.edgeverse.wallet.feature_staking_impl.domain.model.StakingPeriod
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.StakeActionsValidationPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.main.StakeActionsValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.alerts.model.AlertModel
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.di.StakingViewStateFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.model.StakingNetworkInfoModel
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.AssetSelectorMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.WithAssetSelector
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.selectedChainFlow
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal

private const val CURRENT_ICON_SIZE = 40

private val WARNING_ICON = R.drawable.ic_warning_filled
private val WAITING_ICON = R.drawable.ic_time_24

class StakingViewModel(
    private val interactor: StakingInteractor,
    private val alertsInteractor: AlertsInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val stakingViewStateFactory: StakingViewStateFactory,
    private val router: StakingRouter,
    private val resourceManager: ResourceManager,
    private val redeemValidationSystem: StakeActionsValidationSystem,
    private val bondMoreValidationSystem: StakeActionsValidationSystem,
    private val validationExecutor: ValidationExecutor,
    stakingUpdateSystem: UpdateSystem,
    assetSelectorMixinFactory: MixinFactory<AssetSelectorMixin.Presentation>,
    selectedAssetState: SingleAssetSharedState,
) : BaseViewModel(),
    WithAssetSelector,
    Validatable by validationExecutor {

    override val assetSelectorMixin = assetSelectorMixinFactory.create(scope = this)

    private val stakingStateScope = viewModelScope.childScope(supervised = true)

    private val selectionState = interactor.selectionStateFlow()
        .share()

    private val loadingStakingState = selectionState
        .withLoading { (account, assetWithToken) ->
            interactor.selectedAccountStakingStateFlow(account, assetWithToken)
        }.share()

    val stakingViewStateFlow = loadingStakingState
        .onEach { stakingStateScope.coroutineContext.cancelChildren() }
        .mapLoading(::transformStakingState)
        .inBackground()
        .share()

    private val selectedChain = selectedAssetState.selectedChainFlow()
        .share()

    val networkInfoStateLiveData = selectedChain
        .distinctUntilChanged()
        .withLoading { chain ->
            interactor.observeNetworkInfoState(chain.id).combine(assetSelectorMixin.selectedAssetFlow) { networkInfo, asset ->
                transformNetworkInfo(asset, networkInfo)
            }
        }
        .inBackground()
        .asLiveData()

    val currentAddressModelLiveData = currentAddressModelFlow().asLiveData()

    val alertsFlow = loadingStakingState
        .flatMapLoading {
            alertsInteractor.getAlertsFlow(it)
                .mapList(::mapAlertToAlertModel)
        }
        .inBackground()
        .asLiveData()

    init {
        stakingUpdateSystem.start()
            .launchIn(this)
    }

    fun avatarClicked() {
        router.openChangeAccount()
    }

    private fun mapAlertToAlertModel(alert: Alert): AlertModel {
        return when (alert) {
            Alert.ChangeValidators -> {
                AlertModel(
                    WARNING_ICON,
                    resourceManager.getString(R.string.staking_alert_change_validators),
                    resourceManager.getString(R.string.staking_nominator_status_alert_no_validators),
                    AlertModel.Type.CallToAction { router.openCurrentValidators() }
                )
            }

            is Alert.RedeemTokens -> {
                AlertModel(
                    WARNING_ICON,
                    resourceManager.getString(R.string.staking_alert_redeem_title),
                    formatAlertTokenAmount(alert.amount, alert.token),
                    AlertModel.Type.CallToAction(::redeemAlertClicked)
                )
            }

            is Alert.BondMoreTokens -> {
                val existentialDepositDisplay = formatAlertTokenAmount(alert.minimalStake, alert.token)

                AlertModel(
                    WARNING_ICON,
                    resourceManager.getString(R.string.staking_alert_bond_more_title),
                    resourceManager.getString(R.string.staking_alert_bond_more_message, existentialDepositDisplay),
                    AlertModel.Type.CallToAction(::bondMoreAlertClicked)
                )
            }

            is Alert.WaitingForNextEra -> AlertModel(
                WAITING_ICON,
                resourceManager.getString(R.string.staking_nominator_status_alert_waiting_message),
                resourceManager.getString(R.string.staking_alert_start_next_era_message),
                AlertModel.Type.Info
            )
            Alert.SetValidators -> AlertModel(
                WARNING_ICON,
                resourceManager.getString(R.string.staking_set_validators_title),
                resourceManager.getString(R.string.staking_set_validators_message),
                AlertModel.Type.CallToAction { router.openCurrentValidators() }
            )
        }
    }

    private fun formatAlertTokenAmount(amount: BigDecimal, token: Token): String {
        val formattedFiat = token.fiatAmount(amount).formatAsCurrency()
        val formattedAmount = amount.formatTokenAmount(token.configuration)

        return buildString {
            append(formattedAmount)

            formattedFiat.let {
                append(" ($it)")
            }
        }
    }

    private fun bondMoreAlertClicked() = requireValidManageStakingAction(bondMoreValidationSystem) {
        router.openBondMore()
    }

    private fun redeemAlertClicked() = requireValidManageStakingAction(redeemValidationSystem) {
        router.openRedeem()
    }

    private fun requireValidManageStakingAction(
        validationSystem: StakeActionsValidationSystem,
        action: () -> Unit,
    ) = launch {
        val stakingState = (loadingStakingState.first() as? LoadingState.Loaded)?.data
        val stashState = stakingState as? StakingState.Stash ?: return@launch

        validationExecutor.requireValid(
            validationSystem,
            StakeActionsValidationPayload(stashState),
            validationFailureTransformer = { mainStakingValidationFailure(it, resourceManager) }
        ) {
            action()
        }
    }

    private fun transformStakingState(accountStakingState: StakingState) = when (accountStakingState) {
        is StakingState.Stash.Nominator -> stakingViewStateFactory.createNominatorViewState(
            accountStakingState,
            assetSelectorMixin.selectedAssetFlow,
            stakingStateScope,
            ::showError
        )

        is StakingState.Stash.None -> stakingViewStateFactory.createStashNoneState(
            assetSelectorMixin.selectedAssetFlow,
            accountStakingState,
            stakingStateScope,
            ::showError
        )

        is StakingState.NonStash -> stakingViewStateFactory.createWelcomeViewState(
            stakingStateScope,
            ::showError,
            assetSelectorMixin.selectedAssetFlow,
        )

        is StakingState.Stash.Validator -> stakingViewStateFactory.createValidatorViewState(
            accountStakingState,
            assetSelectorMixin.selectedAssetFlow,
            stakingStateScope,
            ::showError
        )
    }

    private fun transformNetworkInfo(asset: Asset, networkInfo: NetworkInfo): StakingNetworkInfoModel {
        val totalStaked = mapAmountToAmountModel(networkInfo.totalStake, asset)
        val minimumStake = mapAmountToAmountModel(networkInfo.minimumStake, asset)

        val unstakingPeriod = resourceManager.getQuantityString(R.plurals.staking_main_lockup_period_value, networkInfo.lockupPeriodInDays)
            .format(networkInfo.lockupPeriodInDays)

        val stakingPeriod = when (networkInfo.stakingPeriod) {
            StakingPeriod.Unlimited -> resourceManager.getString(R.string.common_unlimited)
        }

        return with(networkInfo) {
            StakingNetworkInfoModel(
                totalStaked = totalStaked,
                minimumStake = minimumStake,
                activeNominators = nominatorsCount.format(),
                stakingPeriod = stakingPeriod,
                unstakingPeriod = unstakingPeriod
            )
        }
    }

    private fun currentAddressModelFlow(): Flow<AddressModel> {
        return interactor.selectedAccountProjectionFlow().map {
            addressIconGenerator.createAddressModel(it.address, CURRENT_ICON_SIZE, it.name)
        }
    }
}
