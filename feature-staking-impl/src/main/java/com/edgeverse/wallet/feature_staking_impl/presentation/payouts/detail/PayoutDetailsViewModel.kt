package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.detail

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAccountAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PayoutDetailsViewModel(
    private val interactor: StakingInteractor,
    private val router: StakingRouter,
    private val payout: PendingPayoutParcelable,
    private val addressModelGenerator: AddressIconGenerator,
    private val externalActions: ExternalActions.Presentation,
    private val resourceManager: ResourceManager,
    private val selectedAssetState: SingleAssetSharedState,
) : BaseViewModel(), ExternalActions.Presentation by externalActions {

    val payoutDetails = interactor.currentAssetFlow()
        .map(::mapPayoutParcelableToPayoutDetailsModel)
        .inBackground()
        .asLiveData()

    fun backClicked() {
        router.back()
    }

    fun payoutClicked() {
        val payload = ConfirmPayoutPayload(
            totalRewardInPlanks = payout.amountInPlanks,
            payouts = listOf(payout)
        )

        router.openConfirmPayout(payload)
    }

    fun validatorExternalActionClicked() = launch {
        externalActions.showExternalActions(ExternalActions.Type.Address(payout.validatorInfo.address), selectedAssetState.chain())
    }

    private suspend fun mapPayoutParcelableToPayoutDetailsModel(asset: Asset): PayoutDetailsModel {
        val addressModel = with(payout.validatorInfo) {
            addressModelGenerator.createAccountAddressModel(selectedAssetState.chain(), address, identityName)
        }

        return PayoutDetailsModel(
            validatorAddressModel = addressModel,
            timeLeft = payout.timeLeft,
            timeLeftCalculatedAt = payout.timeLeftCalculatedAt,
            eraDisplay = resourceManager.getString(R.string.staking_era_index_no_prefix, payout.era.toLong()),
            reward = mapAmountToAmountModel(payout.amountInPlanks, asset),
            timerColor = if (payout.closeToExpire) R.color.red else R.color.white,
        )
    }
}
