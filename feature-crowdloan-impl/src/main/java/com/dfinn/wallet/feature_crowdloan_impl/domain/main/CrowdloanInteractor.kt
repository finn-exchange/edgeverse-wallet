package com.dfinn.wallet.feature_crowdloan_impl.domain.main

import com.dfinn.wallet.common.list.GroupedList
import com.dfinn.wallet.common.utils.flowOf
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.model.accountIdIn
import com.dfinn.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.dfinn.wallet.feature_crowdloan_api.data.repository.getContributions
import com.dfinn.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.mapFundInfoToCrowdloan
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.repository.ChainStateRepository
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass
import kotlin.time.ExperimentalTime

typealias GroupedCrowdloans = GroupedList<KClass<out Crowdloan.State>, Crowdloan>

class CrowdloanInteractor(
    private val accountRepository: AccountRepository,
    private val crowdloanRepository: CrowdloanRepository,
    private val chainStateRepository: ChainStateRepository,
    private val externalContributionsSource: ExternalContributionSource,
) {

    fun crowdloansFlow(chain: Chain): Flow<List<Crowdloan>> {
        return flow {
            val accountId = currentAccountIdIn(chain)

            emitAll(crowdloanListFlow(chain, accountId))
        }
    }

    fun groupCrowdloans(crowdloans: List<Crowdloan>): GroupedCrowdloans {
        return crowdloans.groupBy { it.state::class }
            .toSortedMap(Crowdloan.State.STATE_CLASS_COMPARATOR)
    }

    fun externalContributions(chain: Chain): Flow<Int> {
        return flowOf {
            val accountId = currentAccountIdIn(chain)

            loadExternalContributions(chain, accountId)
        }
    }

    fun allUserContributions(
        crowdloans: List<Crowdloan>,
        externalContributions: Int,
    ): Int {
        val directContributions = crowdloans.count { it.myContribution != null }

        return directContributions + externalContributions
    }

    private suspend fun currentAccountIdIn(chain: Chain): AccountId {
        val metaAccount = accountRepository.getSelectedMetaAccount()
        return metaAccount.accountIdIn(chain)!! // TODO ethereum
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun loadExternalContributions(
        chain: Chain,
        accountId: AccountId,
    ): Int {
        return externalContributionsSource.getContributions(chain, accountId).count()
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun crowdloanListFlow(
        chain: Chain,
        accountId: AccountId,
    ): Flow<List<Crowdloan>> {
        val chainId = chain.id

        val parachainMetadatas = runCatching {
            crowdloanRepository.getParachainMetadata(chain)
        }.getOrDefault(emptyMap())

        return chainStateRepository.currentBlockNumberFlow(chain.id).map { currentBlockNumber ->
            val fundInfos = crowdloanRepository.allFundInfos(chainId)

            val directContributions = crowdloanRepository.getContributions(chainId, accountId, fundInfos)

            val winnerInfo = crowdloanRepository.getWinnerInfo(chainId, fundInfos)

            val expectedBlockTime = chainStateRepository.expectedBlockTimeInMillis(chainId)
            val blocksPerLeasePeriod = crowdloanRepository.blocksPerLeasePeriod(chainId)

            fundInfos.values
                .map { fundInfo ->
                    val paraId = fundInfo.paraId

                    mapFundInfoToCrowdloan(
                        fundInfo = fundInfo,
                        parachainMetadata = parachainMetadatas[paraId],
                        parachainId = paraId,
                        currentBlockNumber = currentBlockNumber,
                        expectedBlockTimeInMillis = expectedBlockTime,
                        blocksPerLeasePeriod = blocksPerLeasePeriod,
                        contribution = directContributions[paraId],
                        hasWonAuction = winnerInfo.getValue(paraId)
                    )
                }
                .sortedWith(
                    compareByDescending<Crowdloan> { it.fundInfo.raised }
                        .thenBy { it.fundInfo.end }
                )
        }
    }
}
