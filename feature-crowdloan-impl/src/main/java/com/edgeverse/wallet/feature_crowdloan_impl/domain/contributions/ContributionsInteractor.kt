package com.edgeverse.wallet.feature_crowdloan_impl.domain.contributions

import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.model.accountIdIn
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.getContributions
import com.edgeverse.wallet.feature_crowdloan_impl.data.source.contribution.ExternalContributionSource
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain

class ContributionsInteractor(
    private val externalContributionSource: ExternalContributionSource,
    private val crowdloanRepository: CrowdloanRepository,
    private val accountRepository: AccountRepository,
    private val selectedAssetState: SingleAssetSharedState,
) {

    suspend fun getUserContributions(): List<Contribution> {
        val chain = selectedAssetState.chain()
        val metaAccount = accountRepository.getSelectedMetaAccount()
        val accountId = metaAccount.accountIdIn(chain)!!

        if (crowdloanRepository.isCrowdloansAvailable(chain.id).not()) {
            return emptyList()
        }

        val parachainMetadatas = runCatching {
            crowdloanRepository.getParachainMetadata(chain)
        }.getOrDefault(emptyMap())

        val fundInfos = crowdloanRepository.allFundInfos(chain.id)

        val directContributions = crowdloanRepository.getContributions(chain.id, accountId, fundInfos)
            .mapNotNull { (paraId, directContribution) ->
                directContribution?.let {
                    Contribution(
                        amount = it.amount,
                        paraId = paraId,
                        fundInfo = fundInfos.getValue(paraId),
                        sourceName = null,
                        parachainMetadata = parachainMetadatas[paraId]
                    )
                }
            }

        val externalContributions = externalContributionSource.getContributions(
            chain = chain,
            accountId = accountId
        )
            .map {
                Contribution(
                    amount = it.amount,
                    parachainMetadata = parachainMetadatas[it.paraId],
                    sourceName = it.sourceName,
                    fundInfo = fundInfos.getValue(it.paraId),
                    paraId = it.paraId
                )
            }

        return directContributions + externalContributions
    }
}
