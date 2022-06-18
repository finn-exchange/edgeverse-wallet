package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.confirm

import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.flowOf
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.feature_crowdloan_impl.R
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.acala.ContributionType
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.ConfirmContributeCustomization
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.AcalaCustomizationPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class AcalaConfirmContributeViewStateFactory(
    private val resourceManager: ResourceManager,
) {

    fun create(
        scope: CoroutineScope,
        payload: AcalaCustomizationPayload,
    ) = AcalaConfirmContributeViewState(
        payload = payload,
        resourceManager = resourceManager,
        scope = scope
    )
}

class AcalaConfirmContributeViewState(
    private val payload: AcalaCustomizationPayload,
    private val resourceManager: ResourceManager,
    scope: CoroutineScope,
) : ConfirmContributeCustomization.ViewState, CoroutineScope by scope {

    val contributionTypeFlow = flowOf {
        val stringRes = when (payload.contributionType) {
            ContributionType.LIQUID -> R.string.crowdloan_acala_liquid
            ContributionType.DIRECT -> R.string.crowdloan_acala_direct
        }

        resourceManager.getString(stringRes)
    }.inBackground()
        .shareIn(this, SharingStarted.Eagerly, replay = 1)
}
