package com.dfinn.wallet.feature_assets.presentation.receive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.feature_assets.R
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.QrCodeGenerator
import com.dfinn.wallet.common.utils.flowOf
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.invoke
import com.dfinn.wallet.common.utils.lazyAsync
import com.dfinn.wallet.feature_account_api.data.mappers.mapChainToUi
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.domain.model.addressIn
import com.dfinn.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_assets.domain.receive.ReceiveInteractor
import com.dfinn.wallet.feature_assets.presentation.AssetPayload
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.receive.model.QrSharingPayload
import com.dfinn.wallet.feature_assets.presentation.receive.model.TokenReceiver
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.chainWithAsset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReceiveViewModel(
    private val interactor: ReceiveInteractor,
    private val qrCodeGenerator: QrCodeGenerator,
    private val addressIconGenerator: AddressIconGenerator,
    private val resourceManager: ResourceManager,
    private val externalActions: ExternalActions.Presentation,
    private val assetPayload: AssetPayload,
    private val chainRegistry: ChainRegistry,
    selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
) : BaseViewModel(), ExternalActions by externalActions {

    private val chainWithAssetAsync by lazyAsync {
        chainRegistry.chainWithAsset(assetPayload.chainId, assetPayload.chainAssetId)
    }

    val qrBitmapFlow = flowOf {
        val qrString = interactor.getQrCodeSharingString(assetPayload.chainId)

        qrCodeGenerator.generateQrBitmap(qrString)
    }
        .inBackground()
        .share()

    val receiver = selectedAccountUseCase.selectedMetaAccountFlow()
        .map {
            val (chain, _) = chainWithAssetAsync()
            val address = it.addressIn(chain)!!

            TokenReceiver(
                addressModel = addressIconGenerator.createAddressModel(chain, address, AddressIconGenerator.SIZE_BIG, it.name),
                chain = mapChainToUi(chain),
            )
        }
        .inBackground()
        .share()

    val toolbarTitle = flowOf {
        val (_, chainAsset) = chainWithAssetAsync()

        resourceManager.getString(R.string.wallet_asset_receive_token, chainAsset.symbol)
    }
        .inBackground()
        .share()

    private val _shareEvent = MutableLiveData<Event<QrSharingPayload>>()
    val shareEvent: LiveData<Event<QrSharingPayload>> = _shareEvent

    fun recipientClicked() = launch {
        val accountAddress = receiver.first().addressModel.address
        val (chain, _) = chainWithAssetAsync()

        externalActions.showExternalActions(ExternalActions.Type.Address(accountAddress), chain)
    }

    fun backClicked() {
        router.back()
    }

    fun shareButtonClicked() = launch {
        val qrBitmap = qrBitmapFlow.first()
        val address = receiver.first().addressModel.address
        val (chain, chainAsset) = chainWithAssetAsync()

        viewModelScope.launch {
            interactor.generateTempQrFile(qrBitmap)
                .onSuccess { fileUri ->
                    val message = generateShareMessage(chain, chainAsset, address)

                    _shareEvent.value = Event(QrSharingPayload(fileUri, message))
                }
                .onFailure(::showError)
        }
    }

    private fun generateShareMessage(chain: Chain, tokenType: Chain.Asset, address: String): String {
        return resourceManager.getString(R.string.wallet_receive_share_message).format(
            chain.name,
            tokenType.symbol
        ) + " " + address
    }
}
