package com.dfinn.wallet.feature_crowdloan_impl.domain.main.statefull

import com.dfinn.wallet.common.presentation.LoadingState
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.GroupedCrowdloans
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface StatefulCrowdloanMixin {

    interface Factory {
        fun create(scope: CoroutineScope): StatefulCrowdloanMixin
    }

    val allUserContributions: Flow<LoadingState<Int>>

    val groupedCrowdloansFlow: Flow<LoadingState<GroupedCrowdloans>>
}
