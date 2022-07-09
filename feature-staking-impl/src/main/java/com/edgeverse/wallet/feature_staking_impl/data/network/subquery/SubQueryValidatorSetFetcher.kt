package com.edgeverse.wallet.feature_staking_impl.data.network.subquery

import com.edgeverse.wallet.common.data.network.subquery.EraValidatorInfoQueryResponse.EraValidatorInfo.Nodes.Node
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_api.domain.api.historicalEras
import com.edgeverse.wallet.feature_staking_impl.data.network.subquery.request.StakingEraValidatorInfosRequest
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

class SubQueryValidatorSetFetcher(
    private val stakingApi: StakingApi,
    private val stakingRepository: StakingRepository,
) {

    suspend fun fetchAllValidators(chain: Chain, stashAccountAddress: String): List<String> {
        val historicalRange = stakingRepository.historicalEras(chain.id)

        val stakingExternalApi = chain.externalApi?.staking ?: return emptyList()

        val validatorsInfos = stakingApi.getValidatorsInfo(
            stakingExternalApi.url,
            StakingEraValidatorInfosRequest(
                eraFrom = historicalRange.first(),
                eraTo = historicalRange.last(),
                accountAddress = stashAccountAddress
            )
        )

        return validatorsInfos.data.query?.eraValidatorInfos?.nodes?.map(
            Node::address
        )?.distinct().orEmpty()
    }
}
