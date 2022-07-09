package com.edgeverse.wallet.feature_account_impl.presentation.view.advanced.network.model

import com.edgeverse.wallet.core.model.Node
import com.edgeverse.wallet.feature_account_impl.R

data class NetworkModel(
    val name: String,
    val networkTypeUI: NetworkTypeUI
) {
    sealed class NetworkTypeUI(val icon: Int, val networkType: Node.NetworkType) {
        object Kusama : NetworkTypeUI(R.drawable.ic_ksm_24, Node.NetworkType.KUSAMA)
        object Polkadot : NetworkTypeUI(R.drawable.ic_polkadot_24, Node.NetworkType.POLKADOT)
        object Westend : NetworkTypeUI(R.drawable.ic_westend_24, Node.NetworkType.WESTEND)
        object Rococo : NetworkTypeUI(R.drawable.ic_polkadot_24, Node.NetworkType.ROCOCO)
    }
}
