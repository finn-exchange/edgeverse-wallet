package com.dfinn.wallet.feature_crowdloan_impl.domain.main.statefull

import com.dfinn.wallet.common.presentation.combineLoading
import com.dfinn.wallet.common.presentation.mapLoading
import com.dfinn.wallet.common.utils.WithCoroutineScopeExtensions
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.withLoading
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.CrowdloanInteractor
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.selectedChainFlow
import kotlinx.coroutines.CoroutineScope

class StatefulCrowdloanProviderFactory(
    private val singleAssetSharedState: SingleAssetSharedState,
    private val interactor: CrowdloanInteractor,
) : StatefulCrowdloanMixin.Factory {

    override fun create(scope: CoroutineScope): StatefulCrowdloanMixin {
        return StatefulCrowdloanProvider(
            singleAssetSharedState = singleAssetSharedState,
            interactor = interactor,
            coroutineScope = scope
        )
    }
}

class StatefulCrowdloanProvider(
    singleAssetSharedState: SingleAssetSharedState,
    private val interactor: CrowdloanInteractor,
    coroutineScope: CoroutineScope,
) : StatefulCrowdloanMixin,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    private val selectedChain = singleAssetSharedState.selectedChainFlow()
        .inBackground()
        .share()

    private val crowdloansIntermediateState = selectedChain.withLoading(interactor::crowdloansFlow)
        .inBackground()
        .share()

    private val externalContributionsIntermediateState = selectedChain.withLoading(interactor::externalContributions)
        .inBackground()
        .share()

    override val groupedCrowdloansFlow = crowdloansIntermediateState.mapLoading {
        interactor.groupCrowdloans(it)
    }
        .inBackground()
        .share()

    override val allUserContributions = combineLoading(
        crowdloansIntermediateState,
        externalContributionsIntermediateState,
        interactor::allUserContributions
    )
        .inBackground()
        .share()
}
