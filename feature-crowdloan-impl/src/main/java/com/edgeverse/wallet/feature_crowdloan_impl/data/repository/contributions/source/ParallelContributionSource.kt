package com.edgeverse.wallet.feature_crowdloan_impl.data.repository.contributions.source

import android.util.Log
import com.edgeverse.wallet.common.utils.LOG_TAG
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parallel.ParallelApi
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parallel.getContributions
import com.edgeverse.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.edgeverse.wallet.runtime.ext.Geneses
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId

class ParallelContributionSource(
    private val parallelApi: ParallelApi,
) : ExternalContributionSource {

    override val supportedChains = setOf(Chain.Geneses.POLKADOT)

    override suspend fun getContributions(
        chain: Chain,
        accountId: AccountId,
    ): List<ExternalContributionSource.Contribution> = runCatching {
        parallelApi.getContributions(
            chain = chain,
            accountId = accountId
        ).map {
            ExternalContributionSource.Contribution(
                sourceName = "Parallel",
                amount = it.amount,
                paraId = it.paraId
            )
        }
    }.getOrElse {
        Log.e(LOG_TAG, "Failed to fetch parallel contributions: ${it.message}")

        emptyList()
    }
}
