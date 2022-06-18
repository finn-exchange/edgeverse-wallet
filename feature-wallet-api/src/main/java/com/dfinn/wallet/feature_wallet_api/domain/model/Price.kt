package com.dfinn.wallet.feature_wallet_api.domain.model

import java.math.BigInteger

class Price(
    val amount: BigInteger,
    val token: Token
)
