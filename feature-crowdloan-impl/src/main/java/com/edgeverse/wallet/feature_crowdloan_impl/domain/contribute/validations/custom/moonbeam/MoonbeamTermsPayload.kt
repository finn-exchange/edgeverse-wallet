package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam

import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import java.math.BigDecimal

class MoonbeamTermsPayload(
    val fee: BigDecimal,
    val asset: Asset
)
