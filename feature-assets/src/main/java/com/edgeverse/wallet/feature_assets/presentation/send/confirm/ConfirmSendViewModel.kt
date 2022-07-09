package com.edgeverse.wallet.feature_assets.presentation.send.confirm

import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.Validatable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.invoke
import com.edgeverse.wallet.common.utils.lazyAsync
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.progressConsumer
import com.edgeverse.wallet.common.view.ButtonState
import com.edgeverse.wallet.feature_account_api.data.mappers.mapChainToUi
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.domain.model.requireAddressIn
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.feature_assets.domain.WalletInteractor
import com.edgeverse.wallet.feature_assets.domain.send.SendInteractor
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.send.TransferDraft
import com.edgeverse.wallet.feature_assets.presentation.send.mapAssetTransferValidationFailureToUI
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferPayload
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.WithFeeLoaderMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.create
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountSign
import com.edgeverse.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.asset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ConfirmSendViewModel(
    private val interactor: WalletInteractor,
    private val sendInteractor: SendInteractor,
    private val router: WalletRouter,
    private val addressIconGenerator: AddressIconGenerator,
    private val externalActions: ExternalActions.Presentation,
    private val chainRegistry: ChainRegistry,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val addressDisplayUseCase: AddressDisplayUseCase,
    private val resourceManager: ResourceManager,
    private val validationExecutor: ValidationExecutor,
    private val walletUiUseCase: WalletUiUseCase,
    feeLoaderMixinFactory: FeeLoaderMixin.Factory,
    val transferDraft: TransferDraft,
) : BaseViewModel(),
    ExternalActions by externalActions,
    Validatable by validationExecutor,
    WithFeeLoaderMixin {

    private val chain by lazyAsync { chainRegistry.getChain(transferDraft.assetPayload.chainId) }
    private val chainAsset by lazyAsync { chainRegistry.asset(transferDraft.assetPayload.chainId, transferDraft.assetPayload.chainAssetId) }

    private val assetFlow = interactor.assetFlow(transferDraft.assetPayload.chainId, transferDraft.assetPayload.chainAssetId)
        .inBackground()
        .share()

    private val commissionAssetFlow = interactor.commissionAssetFlow(transferDraft.assetPayload.chainId)
        .inBackground()
        .share()

    override val feeLoaderMixin: FeeLoaderMixin.Presentation = feeLoaderMixinFactory.create(commissionAssetFlow)

    private val currentAccount = selectedAccountUseCase.selectedMetaAccountFlow()
        .inBackground()
        .share()

    val recipientModel = flowOf {
        createAddressModel(transferDraft.recipientAddress, resolveName = true)
    }
        .inBackground()
        .share()

    val senderModel = currentAccount.mapLatest { metaAccount ->
        createAddressModel(metaAccount.requireAddressIn(chain()), resolveName = false)
    }
        .inBackground()
        .share()

    val amountModel = assetFlow.map { asset ->
        mapAmountToAmountModel(transferDraft.amount, asset, tokenAmountSign = AmountSign.NEGATIVE)
    }

    val chainUi = flowOf { mapChainToUi(chain()) }
        .share()

    val wallet = walletUiUseCase.selectedWalletUiFlow()
        .inBackground()
        .share()

    private val _transferSubmittingLiveData = MutableStateFlow(false)

    val sendButtonStateLiveData = _transferSubmittingLiveData.map { submitting ->
        if (submitting) {
            ButtonState.PROGRESS
        } else {
            ButtonState.NORMAL
        }
    }

    init {
        setInitialState()
    }

    fun backClicked() {
        router.back()
    }

    fun recipientAddressClicked() = launch {
        showExternalActions(transferDraft.recipientAddress)
    }

    fun senderAddressClicked() = launch {
        showExternalActions(senderModel.first().address)
    }

    private suspend fun showExternalActions(address: String) {
        externalActions.showExternalActions(ExternalActions.Type.Address(address), chain())
    }

    fun submitClicked() = launch {
        val payload = buildValidationPayload()

        validationExecutor.requireValid(
            validationSystem = sendInteractor.validationSystemFor(payload.transfer.chainAsset),
            payload = payload,
            progressConsumer = _transferSubmittingLiveData.progressConsumer(),
            validationFailureTransformer = { mapAssetTransferValidationFailureToUI(resourceManager, it) }
        ) { validPayload ->
            performTransfer(validPayload.transfer, validPayload.fee)
        }
    }

    private fun setInitialState() = launch {
        feeLoaderMixin.setFee(transferDraft.fee)
    }

    private suspend fun createAddressModel(address: String, resolveName: Boolean) =
        addressIconGenerator.createAddressModel(
            chain = chain(),
            sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
            address = address,
            background = AddressIconGenerator.BACKGROUND_TRANSPARENT,
            addressDisplayUseCase = addressDisplayUseCase.takeIf { resolveName }
        )

    private fun performTransfer(transfer: AssetTransfer, fee: BigDecimal) = launch {
        sendInteractor.performTransfer(transfer, fee)
            .onSuccess {
                showMessage(resourceManager.getString(R.string.common_transaction_submitted))

                router.finishSendFlow()
            }.onFailure(::showError)

        _transferSubmittingLiveData.value = false
    }

    private suspend fun buildValidationPayload(): AssetTransferPayload {
        val chain = chain()
        val chainAsset = chainAsset()

        return AssetTransferPayload(
            transfer = AssetTransfer(
                sender = currentAccount.first(),
                recipient = transferDraft.recipientAddress,
                chain = chain,
                chainAsset = chainAsset,
                amount = transferDraft.amount
            ),
            fee = transferDraft.fee,
            commissionAsset = commissionAssetFlow.first(),
            usedAsset = assetFlow.first()
        )
    }
}
