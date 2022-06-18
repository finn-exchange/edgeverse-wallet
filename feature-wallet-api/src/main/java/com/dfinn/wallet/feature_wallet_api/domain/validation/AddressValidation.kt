package com.dfinn.wallet.feature_wallet_api.domain.validation

import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationStatus
import com.dfinn.wallet.common.validation.ValidationSystemBuilder
import com.dfinn.wallet.common.validation.validOrError
import com.dfinn.wallet.runtime.ext.isValidAddress
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain

class AddressValidation<P, E>(
    private val address: (P) -> String,
    private val chain: (P) -> Chain,
    private val error: (P) -> E
) : Validation<P, E> {

    override suspend fun validate(value: P): ValidationStatus<E> {
        return validOrError(chain(value).isValidAddress(address(value))) {
            error(value)
        }
    }
}

fun <P, E> ValidationSystemBuilder<P, E>.validAddress(
    address: (P) -> String,
    chain: (P) -> Chain,
    error: (P) -> E
) = validate(
    AddressValidation(
        address = address,
        chain = chain,
        error = error
    )
)
