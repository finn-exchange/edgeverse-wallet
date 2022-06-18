package com.dfinn.wallet.feature_account_api.data.mappers

import com.dfinn.wallet.core.model.Network
import com.dfinn.wallet.core.model.Node
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId

fun stubNetwork(chainId: ChainId): Network {
    val networkType = Node.NetworkType.findByGenesis(chainId) ?: Node.NetworkType.POLKADOT

    return Network(networkType)
}
