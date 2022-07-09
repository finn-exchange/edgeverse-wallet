package com.edgeverse.wallet.feature_wallet_api.domain.validation

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.ValidationSystemBuilder
import java.math.BigDecimal

class PositiveAmountValidation<P, E>(
    val amountExtractor: (P) -> BigDecimal,
    val errorProvider: () -> E
) : Validation<P, E> {

    override suspend fun validate(value: P): ValidationStatus<E> {
        return if (amountExtractor(value) > BigDecimal.ZERO) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.ERROR, errorProvider())
        }
    }
}

fun <P, E> ValidationSystemBuilder<P, E>.positiveAmount(
    amount: (P) -> BigDecimal,
    error: () -> E
) = validate(
    PositiveAmountValidation(
        amountExtractor = amount,
        errorProvider = error
    )
)
