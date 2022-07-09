package com.edgeverse.wallet.feature_account_api.data.mappers

import com.edgeverse.wallet.core.model.Network
import com.edgeverse.wallet.core.model.Node
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId

fun stubNetwork(chainId: ChainId): Network {
    val networkType = Node.NetworkType.findByGenesis(chainId) ?: Node.NetworkType.POLKADOT

    return Network(networkType)
}
