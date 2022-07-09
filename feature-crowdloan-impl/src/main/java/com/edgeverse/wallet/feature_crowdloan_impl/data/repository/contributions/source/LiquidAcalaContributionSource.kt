package com.edgeverse.wallet.feature_crowdloan_impl.data.repository.contributions.source

import android.util.Log
import com.edgeverse.wallet.common.utils.LOG_TAG
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.acala.AcalaApi
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.acala.getContributions
import com.edgeverse.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.edgeverse.wallet.runtime.ext.Geneses
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import java.math.BigInteger

class LiquidAcalaContributionSource(
    private val acalaApi: AcalaApi,
) : ExternalContributionSource {

    override val supportedChains = setOf(Chain.Geneses.POLKADOT)

    override suspend fun getContributions(
        chain: Chain,
        accountId: AccountId,
    ): List<ExternalContributionSource.Contribution> = runCatching {
        val amount = acalaApi.getContributions(
            chain = chain,
            accountId = accountId
        ).proxyAmount

        listOfNotNull(
            ExternalContributionSource.Contribution(
                sourceName = "Liquid",
                amount = amount!!,
                paraId = 2000.toBigInteger()
            ).takeIf { amount > BigInteger.ZERO }
        )
    }.getOrElse {
        Log.e(LOG_TAG, "Failed to fetch acala contributions: ${it.message}")

        emptyList()
    }
}
