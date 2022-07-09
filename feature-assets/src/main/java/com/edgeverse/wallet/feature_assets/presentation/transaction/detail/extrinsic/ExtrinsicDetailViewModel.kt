package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.extrinsic

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

class ExtrinsicDetailViewModel(
    private val addressDisplayUseCase: AddressDisplayUseCase,
    private val addressIconGenerator: AddressIconGenerator,
    private val chainRegistry: ChainRegistry,
    private val router: WalletRouter,
    val operation: OperationParcelizeModel.Extrinsic,
    private val externalActions: ExternalActions.Presentation
) : BaseViewModel(),
    ExternalActions by externalActions {

    private val chain by lazyAsync {
        chainRegistry.getChain(operation.chainId)
    }

    val senderAddressModelFlow = flowOf {
        getIcon(operation.originAddress)
    }
        .inBackground()
        .share()

    val chainUi = flowOf {
        mapChainToUi(chain())
    }
        .inBackground()
        .share()

    private suspend fun getIcon(address: String) = addressIconGenerator.createAddressModel(
        chain = chain(),
        address = address,
        sizeInDp = AddressIconGenerator.SIZE_BIG,
        addressDisplayUseCase = addressDisplayUseCase,
        background = AddressIconGenerator.BACKGROUND_TRANSPARENT
    )

    fun extrinsicClicked() = launch {
        externalActions.showExternalActions(ExternalActions.Type.Extrinsic(operation.hash), chain())
    }

    fun fromAddressClicked() = launch {
        externalActions.showExternalActions(ExternalActions.Type.Address(operation.originAddress), chain())
    }

    fun backClicked() {
        router.back()
    }
}
