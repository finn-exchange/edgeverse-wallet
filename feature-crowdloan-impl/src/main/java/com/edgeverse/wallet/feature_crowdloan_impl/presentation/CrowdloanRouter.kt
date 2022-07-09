package com.edgeverse.wallet.feature_crowdloan_impl.presentation

import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.confirm.parcel.ConfirmContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import kotlinx.coroutines.flow.Flow

interface CrowdloanRouter {

    fun openContribute(payload: ContributePayload)

    val customBonusFlow: Flow<BonusPayload?>

    val latestCustomBonus: BonusPayload?

    fun openCustomContribute(payload: CustomContributePayload)

    fun setCustomBonus(payload: BonusPayload)

    fun openConfirmContribute(payload: ConfirmContributePayload)

    fun back()

    fun returnToMain()

    fun openMoonbeamFlow(payload: ContributePayload)
    fun openAddAccount(payload: AddAccountPayload)

    fun openUserContributions()
}
