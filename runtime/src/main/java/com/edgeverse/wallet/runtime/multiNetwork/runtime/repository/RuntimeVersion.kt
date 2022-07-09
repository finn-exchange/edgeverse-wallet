package com.edgeverse.wallet.runtime.multiNetwork.runtime.repository

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId

class RuntimeVersion(
    val chainId: ChainId,
    val specVersion: Int
)
