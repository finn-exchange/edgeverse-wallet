package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Retriable
import com.edgeverse.wallet.common.mixin.api.RetryPayload
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.formatAsCurrency
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.requireException
import com.edgeverse.wallet.common.utils.requireValue
import com.edgeverse.wallet.common.utils.singleReplaySharedFlow
import com.edgeverse.wallet.common.utils.withLoading
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.model.PendingPayout
import com.edgeverse.wallet.feature_staking_impl.domain.model.PendingPayoutsStatistics
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.model.PendingPayoutModel
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.model.PendingPayoutsStatisticsModel
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenChange
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PayoutsListViewModel(
    private val router: StakingRouter,
    private val resourceManager: ResourceManager,
    private val interactor: StakingInteractor,
) : BaseViewModel(), Retriable {

    override val retryEvent: MutableLiveData<Event<RetryPayload>> = MutableLiveData()

    private val payoutsStatisticsFlow = singleReplaySharedFlow<PendingPayoutsStatistics>()

    val payoutsStatisticsState = payoutsStatisticsFlow
        .map(::convertToUiModel)
        .withLoading()
        .inBackground()

    init {
        loadPayouts()
    }

    fun backClicked() {
        router.back()
    }

    fun payoutAllClicked() {
        launch {
            val payoutStatistics = payoutsStatisticsFlow.first()

            val payload = ConfirmPayoutPayload(
                totalRewardInPlanks = payoutStatistics.totalAmountInPlanks,
                payouts = payoutStatistics.payouts.map { mapPayoutToParcelable(it) }
            )

            router.openConfirmPayout(payload)
        }
    }

    fun payoutClicked(index: Int) {
        launch {
            val payouts = payoutsStatisticsFlow.first().payouts
            val payout = payouts[index]

            val payoutParcelable = mapPayoutToParcelable(payout)

            router.openPayoutDetails(payoutParcelable)
        }
    }

    private fun loadPayouts() {
        launch {
            val result = interactor.calculatePendingPayouts()

            if (result.isSuccess) {
                payoutsStatisticsFlow.emit(result.requireValue())
            } else {
                val errorMessage = result.requireException().message ?: resourceManager.getString(R.string.common_undefined_error_message)

                retryEvent.value = Event(
                    RetryPayload(
                        title = resourceManager.getString(R.string.common_error_general_title),
                        message = errorMessage,
                        onRetry = ::loadPayouts,
                        onCancel = ::backClicked
                    )
                )
            }
        }
    }

    private suspend fun convertToUiModel(
        statistics: PendingPayoutsStatistics,
    ): PendingPayoutsStatisticsModel {
        val token = interactor.currentAssetFlow().first().token
        val totalAmount = token.amountFromPlanks(statistics.totalAmountInPlanks).formatTokenAmount(token.configuration)

        val payouts = statistics.payouts.map { mapPayoutToPayoutModel(token, it) }

        return PendingPayoutsStatisticsModel(
            payouts = payouts,
            payoutAllTitle = resourceManager.getString(R.string.staking_reward_payouts_payout_all, totalAmount),
            placeholderVisible = payouts.isEmpty()
        )
    }

    private fun mapPayoutToPayoutModel(token: Token, payout: PendingPayout): PendingPayoutModel {
        return with(payout) {
            val amount = token.amountFromPlanks(amountInPlanks)

            PendingPayoutModel(
                validatorTitle = validatorInfo.identityName ?: validatorInfo.address,
                timeLeft = timeLeft,
                createdAt = timeLeftCalculatedAt,
                daysLeftColor = if (closeToExpire) R.color.red else R.color.white_64,
                amount = amount.formatTokenChange(token.configuration, isIncome = true),
                amountFiat = token.fiatAmount(amount).formatAsCurrency()
            )
        }
    }

    private fun mapPayoutToParcelable(payout: PendingPayout): PendingPayoutParcelable {
        return with(payout) {
            PendingPayoutParcelable(
                validatorInfo = PendingPayoutParcelable.ValidatorInfoParcelable(
                    address = validatorInfo.address,
                    identityName = validatorInfo.identityName
                ),
                era = era,
                amountInPlanks = amountInPlanks,
                timeLeftCalculatedAt = timeLeftCalculatedAt,
                timeLeft = timeLeft,
                closeToExpire = closeToExpire
            )
        }
    }
}
