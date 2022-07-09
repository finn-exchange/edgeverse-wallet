package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.bifrost

import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.bifrost.BifrostContributeInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.DefaultReferralCodePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.ReferralCodePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.ReferralContributeViewState
import java.math.BigDecimal

class BifrostContributeViewState(
    interactor: BifrostContributeInteractor,
    customContributePayload: CustomContributePayload,
    resourceManager: ResourceManager,
    termsLink: String,
    private val bonusPercentage: BigDecimal,
    private val bifrostInteractor: BifrostContributeInteractor,
) : ReferralContributeViewState(
    customContributePayload = customContributePayload,
    resourceManager = resourceManager,
    defaultReferralCode = interactor.novaReferralCode,
    bonusPercentage = bonusPercentage,
    termsUrl = termsLink
) {

    override fun createBonusPayload(referralCode: String): ReferralCodePayload {
        return DefaultReferralCodePayload(
            rewardTokenSymbol = customContributePayload.parachainMetadata.token,
            referralCode = referralCode,
            referralBonus = bonusPercentage,
            rewardRate = customContributePayload.parachainMetadata.rewardRate
        )
    }

    override suspend fun validatePayload(payload: ReferralCodePayload) {
        if (bifrostInteractor.isCodeValid(payload.referralCode).not()) {
            throw IllegalArgumentException(resourceManager.getString(R.string.crowdloan_referral_code_invalid))
        }
    }
}
