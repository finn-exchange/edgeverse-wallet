package com.dfinn.wallet.root.presentation.main

import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.root.domain.RootInteractor
import com.dfinn.wallet.runtime.multiNetwork.connection.ChainConnection
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
