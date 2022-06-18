package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral

import com.dfinn.wallet.common.utils.formatAsPercentage
import com.dfinn.wallet.common.utils.fractionToPercentage
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.dfinn.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

interface ReferralCodePayload : BonusPayload {

    val referralCode: String
}

@Parcelize
class DefaultReferralCodePayload(
    override val referralCode: String,
    private val referralBonus: BigDecimal,
    private val rewardTokenSymbol: String,
    private val rewardRate: BigDecimal?,
) : ReferralCodePayload {

    override fun bonusText(amount: BigDecimal): String {
        return if (rewardRate == null) {
            referralBonus.fractionToPercentage().formatAsPercentage()
        } else {
            val bonusReward = amount * rewardRate * referralBonus

            bonusReward.formatTokenAmount(rewardTokenSymbol)
        }
    }
}
