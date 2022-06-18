package com.dfinn.wallet.feature_crowdloan_impl.data.network.api.parallel

import com.dfinn.wallet.runtime.ext.addressOf
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import retrofit2.http.GET
import retrofit2.http.Path

interface ParallelApi {

    companion object {
        const val BASE_URL = "https://auction-service-prod.parallel.fi/crowdloan/rewards/"
    }

    @GET("{network}/{address}")
    suspend fun getContributions(
        @Path("network") network: String,
        @Path("address") address: String,
    ): List<ParallelContribution>
}

private val Chain.networkPath
    get() = name.toLowerCase()

suspend fun ParallelApi.getContributions(chain: Chain, accountId: AccountId) = getContributions(
    network = chain.networkPath,
    address = chain.addressOf(accountId)
)
