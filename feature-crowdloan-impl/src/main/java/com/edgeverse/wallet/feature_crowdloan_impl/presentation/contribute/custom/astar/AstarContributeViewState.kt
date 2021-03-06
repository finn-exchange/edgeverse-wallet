package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.astar

import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.astar.AstarContributeInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.DefaultReferralCodePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.ReferralCodePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.ReferralContributeViewState
import java.math.BigDecimal

class AstarContributeViewState(
    private val interactor: AstarContributeInteractor,
    customContributePayload: CustomContributePayload,
    resourceManager: ResourceManager,
    defaultReferralCode: String,
    private val bonusPercentage: BigDecimal,
    termsLink: String,
) : ReferralContributeViewState(
    customContributePayload = customContributePayload,
    resourceManager = resourceManager,
    defaultReferralCode = defaultReferralCode,
    bonusPercentage = bonusPercentage,
    termsUrl = termsLink
) {

    override fun createBonusPayload(referralCode: String): ReferralCodePayload {
        return DefaultReferralCodePayload(
            rewardTokenSymbol = customContributePayload.parachainMetadata.token,
            referralCode = referralCode,
            rewardRate = customContributePayload.parachainMetadata.rewardRate,
            referralBonus = bonusPercentage
        )
    }

    override suspend fun validatePayload(payload: ReferralCodePayload) {
        val isReferralValid = interactor.isReferralCodeValid(payload.referralCode)

        if (!isReferralValid) throw IllegalArgumentException(resourceManager.getString(R.string.crowdloan_astar_wrong_referral))
    }
}
