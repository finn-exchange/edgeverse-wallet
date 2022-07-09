package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam

import android.os.Parcelable
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.main.Crowdloan
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeSubmitter
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import java.math.BigDecimal

class MoonbeamCrowdloanSubmitter(
    private val interactor: MoonbeamCrowdloanInteractor,
) : CustomContributeSubmitter {

    override suspend fun injectOnChainSubmission(
        crowdloan: Crowdloan,
        customizationPayload: Parcelable?,
        bonusPayload: BonusPayload?,
        amount: BigDecimal,
        extrinsicBuilder: ExtrinsicBuilder,
    ) {
        interactor.additionalSubmission(crowdloan, extrinsicBuilder)
    }

    override suspend fun submitOffChain(
        customizationPayload: Parcelable?,
        bonusPayload: BonusPayload?,
        amount: BigDecimal,
    ) {
        // Do nothing
    }
}
