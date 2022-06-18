package com.dfinn.wallet.feature_dapp_impl.domain.browser.signExtrinsic

import com.dfinn.wallet.common.validation.ValidationSystem
import com.dfinn.wallet.feature_wallet_api.domain.model.Token
import com.dfinn.wallet.feature_wallet_api.domain.model.amountFromPlanks
import java.math.BigDecimal
import java.math.BigInteger

sealed class ConfirmDAppOperationValidationFailure {

    object NotEnoughBalanceToPayFees : ConfirmDAppOperationValidationFailure()
}

class ConfirmDAppOperationValidationPayload(
    val token: Token?
)

inline fun ConfirmDAppOperationValidationPayload.convertingToAmount(planks: () -> BigInteger): BigDecimal {
    require(token != null) {
        "Invalid state - token should be present for validate transaction payload"
    }

    val feeInPlanks = planks()

    return token.amountFromPlanks(feeInPlanks)
}

typealias ConfirmDAppOperationValidationSystem = ValidationSystem<ConfirmDAppOperationValidationPayload, ConfirmDAppOperationValidationFailure>
