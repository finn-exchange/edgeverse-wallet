package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.transfer

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.invoke
import com.edgeverse.wallet.common.utils.lazyAsync
import com.edgeverse.wallet.feature_account_api.data.mappers.mapChainToUi
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    private val router: WalletRouter,
    private val addressIconGenerator: AddressIconGenerator,
    private val addressDisplayUseCase: AddressDisplayUseCase,
    private val chainRegistry: ChainRegistry,
    val operation: OperationParcelizeModel.Transfer,
    private val externalActions: ExternalActions.Presentation,
) : BaseViewModel(),
    ExternalActions by externalActions {

    private val chain by lazyAsync {
        chainRegistry.getChain(operation.chainId)
    }

    val recipientAddressModelFlow = flowOf {
        getIcon(operation.receiver)
    }
        .inBackground()
        .share()

    val senderAddressModelLiveData = flowOf {
        getIcon(operation.sender)
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

    private suspend fun getIcon(address: String): AddressModel {
        return addressIconGenerator.createAddressModel(
            chain = chain(),
            address = address,
            sizeInDp = AddressIconGenerator.SIZE_BIG,
            addressDisplayUseCase = addressDisplayUseCase,
            background = AddressIconGenerator.BACKGROUND_TRANSPARENT
        )
    }

    fun repeatTransaction() {
        val retryAddress = if (operation.isIncome) operation.sender else operation.receiver

        router.openSend(AssetPayload(operation.chainId, operation.assetId), initialRecipientAddress = retryAddress)
    }

    fun transactionHashClicked() = operation.hash?.let {
        showExternalActions(ExternalActions.Type.Extrinsic(it))
    }

    fun fromAddressClicked() {
        showExternalActions(ExternalActions.Type.Address(operation.sender))
    }

    fun toAddressClicked() {
        showExternalActions(ExternalActions.Type.Address(operation.receiver))
    }

    private fun showExternalActions(type: ExternalActions.Type) = launch {
        externalActions.showExternalActions(type, chain())
    }
}
