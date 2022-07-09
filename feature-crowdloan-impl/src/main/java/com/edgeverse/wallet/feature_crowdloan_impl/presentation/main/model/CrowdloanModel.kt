package com.edgeverse.wallet.feature_crowdloan_impl.presentation.main.model

import com.edgeverse.wallet.common.utils.images.Icon
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId

data class CrowdloanStatusModel(
    val status: String,
    val count: String,
)

data class CrowdloanModel(
    val relaychainId: ChainId,
    val parachainId: ParaId,
    val title: String,
    val description: String,
    val icon: Icon,
    val raised: Raised,
    val state: State,
) {

    data class Raised(
        val value: String,
        val percentage: Int, // 0..100
        val percentageDisplay: String,
    )

    sealed class State {
        object Finished : State()

        data class Active(val timeRemaining: String) : State()
    }
}
