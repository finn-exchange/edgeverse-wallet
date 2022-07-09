package com.edgeverse.wallet.feature_staking_impl.presentation.mappers

import androidx.annotation.StringRes
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.formatAsCurrency
import com.edgeverse.wallet.common.utils.formatFractionAsPercentage
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.rewards.PeriodReturns
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.model.RewardEstimation
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount

enum class RewardSuffix(@StringRes val suffixResourceId: Int?) {
    None(null),
    APY(R.string.staking_apy),
    APR(R.string.staking_apr)
}

fun mapPeriodReturnsToRewardEstimation(
    periodReturns: PeriodReturns,
    token: Token,
    resourceManager: ResourceManager,
    rewardSuffix: RewardSuffix = RewardSuffix.None,
): RewardEstimation {

    val gainFormatted = periodReturns.gainFraction.formatFractionAsPercentage()
    val gainWithSuffix = rewardSuffix.suffixResourceId?.let { resourceManager.getString(it, gainFormatted) } ?: gainFormatted

    return RewardEstimation(
        amount = periodReturns.gainAmount.formatTokenAmount(token.configuration),
        fiatAmount = token.fiatAmount(periodReturns.gainAmount).formatAsCurrency(),
        gain = gainWithSuffix
    )
}
