package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.reward

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.invoke
import com.edgeverse.wallet.common.utils.lazyAsync
import com.edgeverse.wallet.feature_account_api.data.mappers.mapChainToUi
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import kotlinx.coroutines.launch

class RewardDetailViewModel(
    val operation: OperationParcelizeModel.Reward,
    private val addressIconGenerator: AddressIconGenerator,
    private val addressDisplayUseCase: AddressDisplayUseCase,
    private val router: WalletRouter,
    private val chainRegistry: ChainRegistry,
    private val externalActions: ExternalActions.Presentation
) : BaseViewModel(),
    ExternalActions by externalActions {

    val chain by lazyAsync {
        chainRegistry.getChain(operation.chainId)
    }

    val validatorAddressModelFlow = flowOf {
        operation.validator?.let { getIcon(it) }
    }
        .inBackground()
        .share()

    val chainUi = flowOf {
        mapChainToUi(chain())
    }
        .inBackground()
        .share()

    fun backClicked() {
        router.back()
    }

    fun eventIdClicked() {
        shoExternalActions(ExternalActions.Type.Event(operation.eventId))
    }

    fun validatorAddressClicked() {
        operation.validator?.let {
            shoExternalActions(ExternalActions.Type.Address(it))
        }
    }

    private fun shoExternalActions(type: ExternalActions.Type) = launch {
        externalActions.showExternalActions(type, chain())
    }

    private suspend fun getIcon(address: String) = addressIconGenerator.createAddressModel(
        chain = chain(),
        address = address,
        sizeInDp = AddressIconGenerator.SIZE_BIG,
        addressDisplayUseCase = addressDisplayUseCase,
        background = AddressIconGenerator.BACKGROUND_TRANSPARENT
    )
}
