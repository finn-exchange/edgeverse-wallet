package com.edgeverse.wallet.root.presentation

import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.R
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.api.NetworkStateMixin
import com.edgeverse.wallet.common.mixin.api.NetworkStateUi
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.root.domain.RootInteractor
import com.edgeverse.wallet.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RootViewModel(
    private val interactor: RootInteractor,
    private val rootRouter: RootRouter,
    private val externalConnectionRequirementFlow: MutableStateFlow<ChainConnection.ExternalRequirement>,
    private val resourceManager: ResourceManager,
    private val networkStateMixin: NetworkStateMixin
) : BaseViewModel(), NetworkStateUi by networkStateMixin {

    private var willBeClearedForLanguageChange = false

    init {
        interactor.runBalancesUpdate()
            .onEach { handleUpdatesSideEffect(it) }
            .launchIn(this)

        updatePhishingAddresses()
    }

    private fun handleUpdatesSideEffect(sideEffect: Updater.SideEffect) {
        // pass
    }

    private fun updatePhishingAddresses() {
        viewModelScope.launch {
            interactor.updatePhishingAddresses()
        }
    }

    fun jsonFileOpened(content: String?) {}

    override fun onCleared() {
        super.onCleared()

        externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.FORBIDDEN
    }

    fun noticeInBackground() {
        if (!willBeClearedForLanguageChange) {
            externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.STOPPED
        }
    }

    fun noticeInForeground() {
        if (externalConnectionRequirementFlow.value == ChainConnection.ExternalRequirement.STOPPED) {
            externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.ALLOWED
        }
    }

    fun noticeLanguageLanguage() {
        willBeClearedForLanguageChange = true
    }

    fun restoredAfterConfigChange() {
        if (willBeClearedForLanguageChange) {
            rootRouter.returnToWallet()

            willBeClearedForLanguageChange = false
        }
    }

    fun externalUrlOpened(uri: String) {
        if (interactor.isBuyProviderRedirectLink(uri)) {
            showMessage(resourceManager.getString(R.string.buy_completed))
        }
    }
}
