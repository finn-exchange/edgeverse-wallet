package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.bifrost

import android.os.Parcelable
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.bifrost.BifrostContributeInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.Crowdloan
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeSubmitter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.DefaultReferralCodePayload
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import java.math.BigDecimal

class BifrostContributeSubmitter(
    private val interactor: BifrostContributeInteractor
) : CustomContributeSubmitter {

    override suspend fun injectOnChainSubmission(
        crowdloan: Crowdloan,
        customizationPayload: Parcelable?,
        bonusPayload: BonusPayload?,
        amount: BigDecimal,
        extrinsicBuilder: ExtrinsicBuilder,
    ) {
        require(bonusPayload is DefaultReferralCodePayload?)

        bonusPayload?.let {
            interactor.submitOnChain(crowdloan.parachainId, bonusPayload.referralCode, extrinsicBuilder)
        }
    }

    override suspend fun submitOffChain(
        customizationPayload: Parcelable?,
        bonusPayload: BonusPayload?,
        amount: BigDecimal,
    ) {
        // Do nothing
    }
}
