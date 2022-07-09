package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam

import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer
import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer.Payload.DialogAction
import com.edgeverse.wallet.common.mixin.api.displayError
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamFlowStatus
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.StartFlowInterceptor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.mapParachainMetadataFromParcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoonbeamStartFlowInterceptor(
    private val crowdloanRouter: CrowdloanRouter,
    private val resourceManager: ResourceManager,
    private val moonbeamInteractor: MoonbeamCrowdloanInteractor,
    private val customDialogDisplayer: CustomDialogDisplayer.Presentation,
) : StartFlowInterceptor {

    override suspend fun startFlow(payload: ContributePayload) {
        withContext(Dispatchers.Default) {
            moonbeamInteractor.flowStatus(mapParachainMetadataFromParcel(payload.parachainMetadata!!))
        }
            .onSuccess { handleMoonbeamStatus(it, payload) }
            .onFailure { customDialogDisplayer.displayError(resourceManager, it) }
    }

    private fun handleMoonbeamStatus(status: MoonbeamFlowStatus, payload: ContributePayload) {
        when (status) {
            MoonbeamFlowStatus.Completed -> crowdloanRouter.openContribute(payload)

            is MoonbeamFlowStatus.NeedsChainAccount -> {
                customDialogDisplayer.displayDialog(
                    CustomDialogDisplayer.Payload(
                        title = resourceManager.getString(R.string.crowdloan_moonbeam_missing_account_title),
                        message = resourceManager.getString(R.string.crowdloan_moonbeam_missing_account_message),
                        okAction = DialogAction(
                            title = resourceManager.getString(R.string.common_add),
                            action = { crowdloanRouter.openAddAccount(AddAccountPayload.ChainAccount(status.chainId, status.metaId)) }
                        ),
                        cancelAction = DialogAction.noOp(resourceManager.getString(R.string.common_cancel))
                    )
                )
            }

            MoonbeamFlowStatus.ReadyToComplete -> crowdloanRouter.openMoonbeamFlow(payload)

            MoonbeamFlowStatus.RegionNotSupported -> {
                customDialogDisplayer.displayDialog(
                    CustomDialogDisplayer.Payload(
                        title = resourceManager.getString(R.string.crowdloan_moonbeam_region_restriction_title),
                        message = resourceManager.getString(R.string.crowdloan_moonbeam_region_restriction_message),
                        okAction = DialogAction.noOp(resourceManager.getString(R.string.common_ok)),
                        cancelAction = null
                    )
                )
            }
            MoonbeamFlowStatus.UnsupportedAccountEncryption -> customDialogDisplayer.displayDialog(
                CustomDialogDisplayer.Payload(
                    title = resourceManager.getString(R.string.crowdloan_moonbeam_encryption_not_supported_title),
                    message = resourceManager.getString(R.string.crowdloan_moonbeam_encryption_not_supported_message),
                    okAction = DialogAction.noOp(resourceManager.getString(R.string.common_ok)),
                    cancelAction = null
                )
            )
        }
    }
}
