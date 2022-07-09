package com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.moonbeam

import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeFactory
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamPrivateSignatureProvider
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeSubmitter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.MoonbeamStartFlowInterceptor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.ConfirmContributeMoonbeamCustomization
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main.SelectContributeMoonbeamCustomization

class MoonbeamContributeFactory(
    override val submitter: CustomContributeSubmitter,
    override val startFlowInterceptor: MoonbeamStartFlowInterceptor,
    override val privateCrowdloanSignatureProvider: MoonbeamPrivateSignatureProvider,
    override val selectContributeCustomization: SelectContributeMoonbeamCustomization,
    override val confirmContributeCustomization: ConfirmContributeMoonbeamCustomization,
) : CustomContributeFactory {

    override val flowType: String = "Moonbeam"
}
