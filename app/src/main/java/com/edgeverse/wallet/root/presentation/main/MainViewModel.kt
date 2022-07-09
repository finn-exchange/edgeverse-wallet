package com.edgeverse.wallet.root.presentation.main

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.root.domain.RootInteractor
import com.edgeverse.wallet.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(
    interactor: RootInteractor,
    externalRequirements: MutableStateFlow<ChainConnection.ExternalRequirement>,
) : BaseViewModel() {

    init {
        externalRequirements.value = ChainConnection.ExternalRequirement.ALLOWED
    }

    val stakingAvailableLiveData = interactor.stakingAvailableFlow()
        .asLiveData()
}
