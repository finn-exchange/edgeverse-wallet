package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.withLoading
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contributions.Contribution
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contributions.ContributionsInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions.model.ContributionModel
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.model.generateCrowdloanIcon
import com.edgeverse.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.edgeverse.wallet.feature_wallet_api.presentation.formatters.formatTokenAmount
import com.edgeverse.wallet.runtime.ext.addressOf
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chainAndAsset
import kotlinx.coroutines.flow.map

class UserContributionsViewModel(
    private val interactor: ContributionsInteractor,
    private val iconGenerator: AddressIconGenerator,
    private val selectedAssetState: SingleAssetSharedState,
    private val resourceManager: ResourceManager,
    private val router: CrowdloanRouter,
) : BaseViewModel() {

    val userContributionsFlow = flowOf { interactor.getUserContributions() }
        .map { contributions ->
            val (chain, chainAsset) = selectedAssetState.chainAndAsset()

            contributions.map { mapCrowdloanToContributionModel(it, chain, chainAsset) }
        }
        .withLoading()
        .inBackground()
        .share()

    fun backClicked() {
        router.back()
    }

    private suspend fun mapCrowdloanToContributionModel(
        contribution: Contribution,
        chain: Chain,
        chainAsset: Chain.Asset,
    ): ContributionModel {
        val depositorAddress = chain.addressOf(contribution.fundInfo.depositor)
        val parachainName = contribution.parachainMetadata?.name ?: contribution.paraId.toString()

        val contributionTitle = if (contribution.sourceName != null) {
            resourceManager.getString(R.string.crowdloan_contributions_with_source, parachainName, contribution.sourceName)
        } else {
            parachainName
        }

        return ContributionModel(
            title = contributionTitle,
            icon = generateCrowdloanIcon(contribution.parachainMetadata, depositorAddress, iconGenerator),
            amount = chainAsset.amountFromPlanks(contribution.amount).formatTokenAmount(chainAsset)
        )
    }
}
