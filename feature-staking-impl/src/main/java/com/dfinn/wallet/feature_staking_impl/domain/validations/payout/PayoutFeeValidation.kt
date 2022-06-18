package com.dfinn.wallet.feature_staking_impl.domain.validations.payout

import com.dfinn.wallet.feature_wallet_api.domain.validation.EnoughToPayFeesValidation

typealias PayoutFeeValidation = EnoughToPayFeesValidation<MakePayoutPayload, PayoutValidationFailure>
