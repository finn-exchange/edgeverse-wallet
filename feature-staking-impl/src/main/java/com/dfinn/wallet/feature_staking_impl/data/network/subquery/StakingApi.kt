package com.dfinn.wallet.feature_staking_impl.data.network.subquery

import com.dfinn.wallet.common.data.network.subquery.EraValidatorInfoQueryResponse
import com.dfinn.wallet.common.data.network.subquery.SubQueryResponse
import com.dfinn.wallet.feature_staking_impl.data.network.subquery.request.StakingEraValidatorInfosRequest
import com.dfinn.wallet.feature_staking_impl.data.network.subquery.request.StakingSumRewardRequest
import com.dfinn.wallet.feature_staking_impl.data.network.subquery.response.SubQueryTotalRewardResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface StakingApi {

    @POST
    suspend fun getTotalReward(
        @Url url: String,
        @Body body: StakingSumRewardRequest
    ): SubQueryResponse<SubQueryTotalRewardResponse>

    @POST
    suspend fun getValidatorsInfo(
        @Url url: String,
        @Body body: StakingEraValidatorInfosRequest
    ): SubQueryResponse<EraValidatorInfoQueryResponse>
}
