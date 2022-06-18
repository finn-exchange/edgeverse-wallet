package com.dfinn.wallet.runtime.multiNetwork.runtime.repository

import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId

class RuntimeVersion(
    val chainId: ChainId,
    val specVersion: Int
)
