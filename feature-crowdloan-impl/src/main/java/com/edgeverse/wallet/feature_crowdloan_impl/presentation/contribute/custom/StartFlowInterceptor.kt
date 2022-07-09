package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom

import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload

interface StartFlowInterceptor {
    suspend fun startFlow(payload: ContributePayload)
}
