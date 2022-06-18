package com.dfinn.wallet.feature_account_api.presenatation.mixin.addressInput

import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.resources.ClipboardManager
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.WithCoroutineScopeExtensions
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.invoke
import com.dfinn.wallet.common.utils.lazyAsync
import com.dfinn.wallet.common.utils.systemCall.ScanQrCodeCall
import com.dfinn.wallet.common.utils.systemCall.SystemCallExecutor
import com.dfinn.wallet.common.utils.systemCall.onSystemCallFailure
import com.dfinn.wallet.feature_account_api.R
import com.dfinn.wallet.runtime.ext.accountIdOf
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import com.dfinn.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddressInputMixinFactory(
    private val chainRegistry: ChainRegistry,
    private val addressIconGenerator: AddressIconGenerator,
    private val systemCallExecutor: SystemCallExecutor,
    private val clipboardManager: ClipboardManager,
    private val resourceManager: ResourceManager,
    private val qrSharingFactory: MultiChainQrSharingFactory,
) {

    fun create(
        chainId: ChainId,
        errorDisplayer: (cause: String) -> Unit,
        coroutineScope: CoroutineScope
    ): AddressInputMixin = AddressInputMixinProvider(
        chainId = chainId,
        chainRegistry = chainRegistry,
        addressIconGenerator = addressIconGenerator,
        systemCallExecutor = systemCallExecutor,
        clipboardManager = clipboardManager,
        qrSharingFactory = qrSharingFactory,
        resourceManager = resourceManager,
        errorDisplayer = errorDisplayer,
        coroutineScope = coroutineScope
    )
}

class AddressInputMixinProvider(
    private val chainId: ChainId,
    private val chainRegistry: ChainRegistry,
    private val addressIconGenerator: AddressIconGenerator,
    private val systemCallExecutor: SystemCallExecutor,
    private val clipboardManager: ClipboardManager,
    private val qrSharingFactory: MultiChainQrSharingFactory,
    private val resourceManager: ResourceManager,
    private val errorDisplayer: (error: String) -> Unit,
    coroutineScope: CoroutineScope,
) : AddressInputMixin,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    private val chain by coroutineScope.lazyAsync {
        chainRegistry.getChain(chainId)
    }

    private val clipboardFlow = clipboardManager.observePrimaryClip()
        .inBackground()
        .share()

    override val inputFlow = MutableStateFlow("")

    override val state = combine(inputFlow, clipboardFlow, ::createState)
        .inBackground()
        .share()

    override fun pasteClicked() {
        launch {
            clipboardFlow.first()?.let {
                inputFlow.value = it
            }
        }
    }

    override fun clearClicked() {
        inputFlow.value = ""
    }

    override fun scanClicked() {
        launch {
            systemCallExecutor.executeSystemCall(ScanQrCodeCall()).mapCatching {
                qrSharingFactory.create(chain()).decode(it).address
            }.onSuccess { address ->
                inputFlow.value = address
            }.onSystemCallFailure {
                errorDisplayer(resourceManager.getString(R.string.invoice_scan_error_no_info))
            }
        }
    }

    private suspend fun createState(input: String, clipboard: String?): AddressInputState {
        val iconState = runCatching {
            val icon = addressIconGenerator.createAddressIcon(
                accountId = chain().accountIdOf(address = input),
                sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
                backgroundColorRes = AddressIconGenerator.BACKGROUND_TRANSPARENT
            )

            AddressInputState.IdenticonState.Address(icon)
        }.getOrDefault(AddressInputState.IdenticonState.Placeholder)

        return AddressInputState(
            iconState = iconState,
            pasteShown = input.isEmpty() && clipboard != null,
            scanShown = input.isEmpty(),
            clearShown = input.isNotEmpty()
        )
    }
}
