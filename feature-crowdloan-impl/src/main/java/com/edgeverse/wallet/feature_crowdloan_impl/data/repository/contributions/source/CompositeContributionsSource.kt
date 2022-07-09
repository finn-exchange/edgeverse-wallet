package com.edgeverse.wallet.feature_crowdloan_impl.data.repository.contributions.source

import com.edgeverse.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.edgeverse.wallet.feature_crowdloan_impl.data.source.contribution.supports
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.runtime.AccountId

class CompositeContributionsSource(
    private val children: Iterable<ExternalContributionSource>,
) : ExternalContributionSource {

    override val supportedChains: Set<ChainId>? = null

    override suspend fun getContributions(
        chain: Chain,
        accountId: AccountId,
    ): List<ExternalContributionSource.Contribution> {
        return children
            .filter { it.supports(chain) }
            .fold(mutableListOf()) { acc, source ->
                val elements = source.getContributions(chain, accountId)
                acc.addAll(elements)

                acc
            }
    }
}
