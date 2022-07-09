package com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.bifrost

import com.edgeverse.wallet.common.data.network.subquery.SubQueryResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface BifrostApi {

    companion object {
        const val BASE_URL = "https://salp-api.bifrost.finance"
    }

    @POST("/")
    suspend fun getAccountByReferralCode(@Body body: BifrostReferralCheckRequest): SubQueryResponse<GetAccountByReferralCodeResponse>
}

suspend fun BifrostApi.getAccountByReferralCode(code: String) = getAccountByReferralCode(BifrostReferralCheckRequest(code))
