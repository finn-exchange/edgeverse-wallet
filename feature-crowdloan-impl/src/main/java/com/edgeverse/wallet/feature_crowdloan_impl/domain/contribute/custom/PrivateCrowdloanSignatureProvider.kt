package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom

import com.edgeverse.wallet.feature_crowdloan_api.data.repository.ParachainMetadata
import java.math.BigInteger

interface PrivateCrowdloanSignatureProvider {

    enum class Mode {
        FEE, SUBMIT
    }

    suspend fun provideSignature(
        chainMetadata: ParachainMetadata,
        previousContribution: BigInteger,
        newContribution: BigInteger,
        address: String,
        mode: Mode,
    ): Any?
}
