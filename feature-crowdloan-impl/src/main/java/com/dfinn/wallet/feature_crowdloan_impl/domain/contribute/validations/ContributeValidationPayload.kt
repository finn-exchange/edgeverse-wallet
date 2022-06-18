package com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations

import android.os.Parcelable
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.Crowdloan
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

class ContributeValidationPayload(
    val crowdloan: Crowdloan,
    val customizationPayload: Parcelable?,
    val asset: Asset,
    val fee: BigDecimal,
    val bonusPayload: BonusPayload?,
    val contributionAmount: BigDecimal,
)
