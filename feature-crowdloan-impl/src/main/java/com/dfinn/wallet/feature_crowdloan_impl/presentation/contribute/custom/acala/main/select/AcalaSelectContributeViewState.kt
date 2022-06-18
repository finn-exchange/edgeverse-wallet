package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.select

import android.os.Parcelable
import com.dfinn.wallet.common.mixin.api.Browserable
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.feature_crowdloan_impl.R
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.acala.ContributionType
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.SelectContributeCustomization
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.acala.main.AcalaCustomizationPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

private val CONTRIBUTION_TYPE_BY_ID = mapOf(
    R.id.acalaContributionTypeDirect to ContributionType.DIRECT,
    R.id.acalaContributionTypeLiquid to ContributionType.LIQUID
)

private val DEFAULT_CONTRIBUTION_TYPE_ID = R.id.acalaContributionTypeDirect

class AcalaSelectContributeViewState(
    private val acalaContributionsInfoLink: String,
    private val browserable: Browserable.Presentation,
    scope: CoroutineScope,
) : SelectContributeCustomization.ViewState, CoroutineScope by scope {

    val selectedContributionTypeIdFlow = MutableStateFlow(DEFAULT_CONTRIBUTION_TYPE_ID)

    fun learnContributionTypesClicked() {
        browserable.showBrowser(acalaContributionsInfoLink)
    }

    override val customizationPayloadFlow: Flow<Parcelable?> = selectedContributionTypeIdFlow
        .map { selectedContributionTypeId ->
            val contributionType = CONTRIBUTION_TYPE_BY_ID.getValue(selectedContributionTypeId)

            AcalaCustomizationPayload(contributionType)
        }
        .inBackground()
        .shareIn(scope, SharingStarted.Eagerly, replay = 1)
}
