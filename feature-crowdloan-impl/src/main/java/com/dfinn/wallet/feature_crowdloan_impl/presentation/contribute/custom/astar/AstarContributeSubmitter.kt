package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.astar

import android.os.Parcelable
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.astar.AstarContributeInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.Crowdloan
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeSubmitter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral.DefaultReferralCodePayload
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import java.math.BigDecimal

class AstarContributeSubmitter(
    private val interactor: AstarContributeInteractor,
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
